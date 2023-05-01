package com.joshkaltman.emceestudios.bankapi.service;

import com.joshkaltman.emceestudios.bankapi.entity.AtmSession;
import com.joshkaltman.emceestudios.bankapi.entity.BankAccount;
import com.joshkaltman.emceestudios.bankapi.exception.NotFoundException;
import com.joshkaltman.emceestudios.bankapi.exception.SessionAlreadyTerminatedException;
import com.joshkaltman.emceestudios.bankapi.repository.AtmSessionRepository;
import com.joshkaltman.emceestudios.bankapi.repository.BankAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.ZonedDateTime;
import java.util.Optional;

@Service
public class SessionService {
    private final AtmSessionRepository atmSessionRepository;
    private final BankAccountRepository bankAccountRepository;

    public SessionService (
            AtmSessionRepository atmSessionRepository,
            BankAccountRepository bankAccountRepository
    ) {
        this.atmSessionRepository = atmSessionRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    public AtmSession createSessionForAccountId(long accountId) throws NotFoundException {
        Optional<AtmSession> activeSession = atmSessionRepository.findActiveSessionByBankAccountId(accountId);
        if (activeSession.isPresent()) {
            AtmSession theSession = activeSession.get();
            theSession.setTerminatesAt(ZonedDateTime.now().plusMinutes(AtmSession.DEFAULT_ATM_SESSION_LENGTH_MINUTES));
            return atmSessionRepository.save(theSession);
        }

        Optional<BankAccount> existingAccount = bankAccountRepository.findById(accountId);
        if (existingAccount.isEmpty()) {
            throw new NotFoundException("Account with id " + accountId + " does not exist");
        }

        AtmSession newSession = AtmSession.builder()
                .bankAccount(existingAccount.get())
                .build();

        return atmSessionRepository.save(newSession);
    }

    public AtmSession terminateAtmSesssion(long accountId, long sessionId)
            throws SessionAlreadyTerminatedException, NotFoundException {
        AtmSession atmSession = getAtmSession(accountId, sessionId);
        if (!atmSession.isActive()) {
            throw new SessionAlreadyTerminatedException("This session has already been terminated");
        }
        atmSession.setTerminatesAt(ZonedDateTime.now());
        return atmSessionRepository.save(atmSession);
    }

    public AtmSession getAtmSession(long accountId, long sessionId)
            throws NotFoundException {
        Optional<AtmSession> session = atmSessionRepository.findById(sessionId);
        if (session.isEmpty() || session.get().getBankAccount().getId() != accountId) {
            throw new NotFoundException("A session with id " + sessionId + " does not exist for bank account " + accountId);
        }
        return session.get();
    }
}
