package com.joshkaltman.emceestudios.bankapi.dto;

import com.joshkaltman.emceestudios.bankapi.entity.BankAccount;
import com.joshkaltman.emceestudios.bankapi.entity.Transaction;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FriendlyTransactionTests {
    @Test
    public void correctlyMapsValuesFromConstructor() {
        Transaction transaction = new Transaction();
        transaction.setAmount(10000);
        transaction.setId(167);
        transaction.setRemarks("Some remarks go here");
        transaction.setSuccessful(true);
        transaction.setBankAccount(new BankAccount());
        transaction.setCreatedAt(ZonedDateTime.now());
        transaction.setTypeCode("AD");

        FriendlyTransaction friendly = new FriendlyTransaction(transaction);

        assertEquals(transaction.getId(), friendly.getId());
        assertEquals(transaction.getRemarks(), friendly.getRemarks());
        assertEquals(transaction.getCreatedAt(), friendly.getCreatedAt());
        assertEquals(transaction.isSuccessful(), friendly.isSuccessful());
        assertEquals("ATM Deposit", friendly.getType());
        assertEquals("100.00", friendly.getAmount());
    }
}
