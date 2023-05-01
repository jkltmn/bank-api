package com.joshkaltman.emceestudios.bankapi.util;

public class Util {
    public static long mapAmountToLong(String amount) {
        return (long) (Double.parseDouble(amount) * 100);
    }

    public static String mapAmountToString(long amount) {
        return String.format("%.2f", (double) amount / 100);
    }

    public static String mapTransactionTypeCodeToString(String typeCode) {
        if (typeCode == null) return null;

        String typeMapping = Constants.transactionCodes.get(typeCode);
        return typeMapping == null ? typeCode : typeMapping;
    }
}
