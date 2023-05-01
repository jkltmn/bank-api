package com.joshkaltman.emceestudios.bankapi.repository;


import com.joshkaltman.emceestudios.bankapi.entity.BankAccount;
import com.joshkaltman.emceestudios.bankapi.entity.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    List<Transaction> findAllByBankAccountOrderByCreatedAtDesc(BankAccount bankAccount);
}
