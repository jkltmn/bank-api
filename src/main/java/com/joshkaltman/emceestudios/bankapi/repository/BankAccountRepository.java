package com.joshkaltman.emceestudios.bankapi.repository;


import com.joshkaltman.emceestudios.bankapi.entity.BankAccount;
import org.springframework.data.repository.CrudRepository;

public interface BankAccountRepository extends CrudRepository<BankAccount, Long> {
}
