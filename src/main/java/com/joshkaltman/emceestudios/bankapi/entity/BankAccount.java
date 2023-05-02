package com.joshkaltman.emceestudios.bankapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankAccount {
    public static final String DEFAULT_BANK_ACCOUNT_CURRENCY = "USD";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonIgnore
    @Column(columnDefinition = "CHAR(4)", nullable = false)
    private String pin;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(columnDefinition = "VARCHAR(3)", nullable = false)
    @Builder.Default
    private String currency = DEFAULT_BANK_ACCOUNT_CURRENCY;

    @Column(nullable = false)
    private long balance;
}