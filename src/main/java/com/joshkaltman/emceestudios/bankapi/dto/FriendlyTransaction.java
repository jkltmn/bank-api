package com.joshkaltman.emceestudios.bankapi.dto;

import com.joshkaltman.emceestudios.bankapi.entity.Transaction;
import com.joshkaltman.emceestudios.bankapi.util.Constants;
import com.joshkaltman.emceestudios.bankapi.util.Util;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.bcel.Const;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendlyTransaction {
    public FriendlyTransaction(Transaction transaction) {
        this.id = transaction.getId();
        this.amount = Util.mapAmountToString(transaction.getAmount());
        this.type = Util.mapTransactionTypeCodeToString(transaction.getTypeCode());
        this.createdAt = transaction.getCreatedAt();
        this.remarks = transaction.getRemarks();
        this.successful = transaction.isSuccessful();
    }

    private long id;

    private String amount;

    private String type;

    private ZonedDateTime createdAt;

    private String remarks;

    private boolean successful;
}
