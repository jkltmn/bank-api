package com.joshkaltman.emceestudios.bankapi.exception;

public class InsufficientFundsException extends Exception {
    public InsufficientFundsException() {
    }

    public InsufficientFundsException(String message) {
        super(message);
    }
}
