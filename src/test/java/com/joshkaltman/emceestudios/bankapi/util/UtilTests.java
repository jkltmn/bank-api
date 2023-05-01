package com.joshkaltman.emceestudios.bankapi.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class UtilTests {
    @Test
    public void mapAmountToLong() {
        assertEquals(25034L, Util.mapAmountToLong("250.34"));
        assertEquals(-1040132, Util.mapAmountToLong("-10401.32"));
    }

    @Test
    public void mapAmountToString() {
        assertEquals("250.34", Util.mapAmountToString(25034L));
        assertEquals("-10401.32", Util.mapAmountToString(-1040132));
    }

    @Test
    public void mapTransactionTypeCodeToStringReturnsMappingIfMappingPresent() {
        assertEquals("ATM Deposit", Util.mapTransactionTypeCodeToString("AD"));
        assertEquals("ATM Withdrawal", Util.mapTransactionTypeCodeToString("AW"));
        assertEquals("Overdraft Fee", Util.mapTransactionTypeCodeToString("FO"));
    }

    @Test
    public void mapTransactionTypeCodeToStringReturnsCodeIfNoMappingPresent() {
        assertEquals("QQQ", Util.mapTransactionTypeCodeToString("QQQ"));
        assertEquals("Q", Util.mapTransactionTypeCodeToString("Q"));
        assertEquals("", Util.mapTransactionTypeCodeToString(""));
        assertNull(Util.mapTransactionTypeCodeToString(null));
    }
}
