package com.joshkaltman.emceestudios.bankapi.service;

import com.joshkaltman.emceestudios.bankapi.dto.FriendlyTransaction;
import com.joshkaltman.emceestudios.bankapi.dto.PerformTransactionRequest;
import com.joshkaltman.emceestudios.bankapi.dto.PerformTransactionResponse;
import com.joshkaltman.emceestudios.bankapi.entity.AtmSession;
import com.joshkaltman.emceestudios.bankapi.entity.BankAccount;
import com.joshkaltman.emceestudios.bankapi.entity.Transaction;
import com.joshkaltman.emceestudios.bankapi.exception.InvalidPinException;
import com.joshkaltman.emceestudios.bankapi.exception.NotFoundException;
import com.joshkaltman.emceestudios.bankapi.repository.AtmSessionRepository;
import com.joshkaltman.emceestudios.bankapi.repository.BankAccountRepository;
import com.joshkaltman.emceestudios.bankapi.repository.TransactionRepository;
import com.joshkaltman.emceestudios.bankapi.util.Constants;
import com.joshkaltman.emceestudios.bankapi.util.Util;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    private final AtmSessionRepository atmSessionRepository;
    private final TransactionRepository transactionRepository;
    private final BankAccountRepository bankAccountRepository;

    public TransactionService(
            AtmSessionRepository atmSessionRepository,
            TransactionRepository transactionRepository,
            BankAccountRepository bankAccountRepository
    ) {
        this.atmSessionRepository = atmSessionRepository;
        this.transactionRepository = transactionRepository;
        this.bankAccountRepository = bankAccountRepository;
    }
    public PerformTransactionResponse performTransaction(long accountId, PerformTransactionRequest req)
            throws NotFoundException, InvalidPinException {
        Optional<AtmSession> session = atmSessionRepository.findActiveSessionByBankAccountId(accountId);
        if (session.isEmpty() || !session.get().isActive()) {
            throw new NotFoundException("No active session exists for bank account " + accountId);
        }

        AtmSession curSession = session.get();
        long requestTransactionAmount = Util.mapAmountToLong(req.getAmount());
        long originalBalance = curSession.getBankAccount().getBalance();
        long targetNewBalance = originalBalance + requestTransactionAmount;

        BankAccount curAccount = curSession.getBankAccount();

        boolean pinMatchesAccount = curSession.getBankAccount().getPin().equals(req.getPin());
        boolean isOverdraft = targetNewBalance < Constants.OVERDRAFT_PERMITTED_W_FEE_FLOOR;
        boolean isOverdraftPermitted = isOverdraft && targetNewBalance >= Constants.OVERDRAFT_REJECTION_FLOOR;
        long actualNewBalance = originalBalance +
                ((!isOverdraft || isOverdraftPermitted) ? requestTransactionAmount : 0) +
                ((isOverdraft) ? Constants.OVERDRAFT_FEE_AMT : 0);

        if (!pinMatchesAccount) {
            throw new InvalidPinException("The PIN provided does not match the PIN for this account");
        }
        else {
            String remarks = (isOverdraft) ?
                    (isOverdraftPermitted) ?
                        "This transaction has resulted in an overdraft. An overdraft fee has been assessed."
                        : "This transaction has resulted in a major overdraft and was not completed. An overdraft fee has been assessed."
                    : null;

            Transaction requested = buildTransaction(curAccount, requestTransactionAmount, remarks, !isOverdraft || isOverdraftPermitted);
            transactionRepository.save(requested);

            if (isOverdraft) {
                Transaction overdraft = buildOverdraftTransaction(curAccount, requested.getId());
                transactionRepository.save(overdraft);
                targetNewBalance += overdraft.getAmount();
            }

            curAccount.setBalance(targetNewBalance);
            bankAccountRepository.save(curAccount);

            return new PerformTransactionResponse(requested, originalBalance, actualNewBalance);
        }
    }

    public List<FriendlyTransaction> getAllTransactionsForBankAccount(long accountId) throws NotFoundException {
        Optional<AtmSession> session = atmSessionRepository.findActiveSessionByBankAccountId(accountId);
        if (session.isEmpty() || !session.get().isActive()) {
            throw new NotFoundException("No active session exists for bank account " + accountId);
        }

        return transactionRepository.findAllByBankAccountOrderByCreatedAtDesc(session.get().getBankAccount())
                .stream()
                .map(FriendlyTransaction::new)
                .toList();
    }

    private Transaction buildTransaction(BankAccount bankAccount, long amount, String remarks, boolean successful) {
        return Transaction.builder()
                .bankAccount(bankAccount)
                .amount(amount)
                .successful(successful)
                .remarks(remarks)
                .typeCode(amount >= 0 ? Constants.TYPE_CODE_ATM_DEPOSIT : Constants.TYPE_CODE_ATM_WITHDRAWAL)
                .build();
    }

    private Transaction buildOverdraftTransaction(BankAccount bankAccount, long originalTransactionId) {
        return Transaction.builder()
                .bankAccount(bankAccount)
                .amount(Constants.OVERDRAFT_FEE_AMT)
                .successful(true)
                .typeCode(Constants.TYPE_CODE_FEE_OVERDRAFT)
                .remarks("Overdraft fee for transaction " + originalTransactionId)
                .build();
    }
}
