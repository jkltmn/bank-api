package com.joshkaltman.emceestudios.bankapi.exception;

public class SessionAlreadyTerminatedException extends Exception {
    public SessionAlreadyTerminatedException() {
    }

    public SessionAlreadyTerminatedException(String message) {
        super(message);
    }
}
