package com.joshkaltman.emceestudios.bankapi.service;

import com.joshkaltman.emceestudios.bankapi.dto.CreateBankAccountRequest;
import com.joshkaltman.emceestudios.bankapi.entity.BankAccount;
import com.joshkaltman.emceestudios.bankapi.repository.BankAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Service
public class BankAccountService {
    BankAccountRepository bankAccountRepository;
    public BankAccountService(
            BankAccountRepository bankAccountRepository
    ) {
        this.bankAccountRepository = bankAccountRepository;
    }
    public Optional<BankAccount> getBankAccountById(long id) {
        return bankAccountRepository.findById(id);
    }

    @PostMapping(path = "/accounts")
    public BankAccount createBankAccount(CreateBankAccountRequest req) {
        BankAccount newAccount = BankAccount.builder()
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .pin(req.getPin())
                .currency(req.getCurrency())
                .build();

        return bankAccountRepository.save(newAccount);
    }
}
