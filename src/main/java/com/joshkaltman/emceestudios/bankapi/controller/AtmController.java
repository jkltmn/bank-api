package com.joshkaltman.emceestudios.bankapi.controller;

import com.joshkaltman.emceestudios.bankapi.dto.CreateBankAccountRequest;
import com.joshkaltman.emceestudios.bankapi.dto.FriendlyTransaction;
import com.joshkaltman.emceestudios.bankapi.dto.PerformTransactionRequest;
import com.joshkaltman.emceestudios.bankapi.dto.PerformTransactionResponse;
import com.joshkaltman.emceestudios.bankapi.entity.AtmSession;
import com.joshkaltman.emceestudios.bankapi.entity.BankAccount;
import com.joshkaltman.emceestudios.bankapi.entity.Transaction;
import com.joshkaltman.emceestudios.bankapi.exception.InvalidPinException;
import com.joshkaltman.emceestudios.bankapi.exception.NotFoundException;
import com.joshkaltman.emceestudios.bankapi.exception.SessionAlreadyTerminatedException;
import com.joshkaltman.emceestudios.bankapi.service.BankAccountService;
import com.joshkaltman.emceestudios.bankapi.service.SessionService;
import com.joshkaltman.emceestudios.bankapi.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/v1/api")
public class AtmController {
    private final BankAccountService bankAccountService;
    private final SessionService sessionService;
    private final TransactionService transactionService;

    public AtmController(
            BankAccountService bankAccountService,
            SessionService sessionService,
            TransactionService transactionService
    ) {
        this.bankAccountService = bankAccountService;
        this.sessionService = sessionService;
        this.transactionService = transactionService;
    }

    @GetMapping(path = "/accounts/{id}")
    public BankAccount getBankAccount(@PathVariable long id) {
        return bankAccountService.getBankAccountById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Could not find account with id " + id)
                );
    }

    @PostMapping(path = "/accounts")
    public BankAccount createBankAccount(@Valid @RequestBody CreateBankAccountRequest req) {
        return bankAccountService.createBankAccount(req);
    }

    @PostMapping(path = "/accounts/{accountId}/sessions")
    public AtmSession createSession(@PathVariable("accountId") long accountId) {
        try {
            return sessionService.createSessionForAccountId(accountId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping(path = "/accounts/{accountId}/sessions/{sessionId}/terminate")
    public AtmSession terminateAtmSesssion(@PathVariable long accountId, @PathVariable long sessionId) {
        try {
            return sessionService.terminateAtmSesssion(accountId, sessionId);
        } catch (SessionAlreadyTerminatedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping(path = "/accounts/{accountId}/sessions/{sessionId}")
    public AtmSession getAtmSesssion(@PathVariable long accountId, @PathVariable long sessionId) {
        try {
            return sessionService.getAtmSession(accountId, sessionId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping(path = "/accounts/{accountId}/transactions")
    public PerformTransactionResponse performTransaction(@PathVariable long accountId, @Valid @RequestBody PerformTransactionRequest req) {
        try {
            return transactionService.performTransaction(accountId, req);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (InvalidPinException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping(path = "/accounts/{accountId}/transactions")
    public List<FriendlyTransaction> getTransactions(@PathVariable long accountId) {
        try {
            return transactionService.getAllTransactionsForBankAccount(accountId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
