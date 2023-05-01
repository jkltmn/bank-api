package com.joshkaltman.emceestudios.bankapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.joshkaltman.emceestudios.bankapi.entity.BankAccount;
import com.joshkaltman.emceestudios.bankapi.entity.Transaction;
import com.joshkaltman.emceestudios.bankapi.util.Util;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

@Data
public class PerformTransactionResponse {
    public PerformTransactionResponse(Transaction transaction, long priorAccountBalance, long newAccountBalance) {
        this.transaction =  new FriendlyTransaction(transaction);
        this.priorAccountBalance = Util.mapAmountToString(priorAccountBalance);
        this.newAccountBalance = Util.mapAmountToString(newAccountBalance);
    }

    private FriendlyTransaction transaction;
    private String priorAccountBalance;
    private String newAccountBalance;
}
