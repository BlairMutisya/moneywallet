package com.cashbox.moneywallet.exception;

public class InvalidAccountTypeException extends RuntimeException {
    public InvalidAccountTypeException(String message) {
        super(message);
    }
}
