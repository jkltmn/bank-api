package com.joshkaltman.emceestudios.bankapi.util;

import java.util.Map;

public class Constants {
    // Overdraft
    public static final long OVERDRAFT_PERMITTED_W_FEE_FLOOR = 0;
    public static final long OVERDRAFT_REJECTION_FLOOR = -100 * 100;
    public static final long OVERDRAFT_FEE_AMT = -15 * 100;

    // Transaction type codes
    public static final String TYPE_CODE_ATM_WITHDRAWAL = "AW";
    public static final String TYPE_CODE_ATM_DEPOSIT = "AD";
    public static final String TYPE_CODE_FEE_OVERDRAFT = "FO";
    public static final Map<String, String> transactionCodes = Map.of(
            TYPE_CODE_ATM_DEPOSIT, "ATM Deposit",
            TYPE_CODE_ATM_WITHDRAWAL, "ATM Withdrawal",
            TYPE_CODE_FEE_OVERDRAFT, "Overdraft Fee"
    );
}
