package com.joshkaltman.emceestudios.bankapi.exception;

public class InvalidPinException extends Exception {
    public InvalidPinException() {
    }

    public InvalidPinException(String message) {
        super(message);
    }
}
