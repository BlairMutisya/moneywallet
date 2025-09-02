package com.cashbox.moneywallet.authservice.exception;

public class AuthException extends RuntimeException {
    private final String errorCode;

    public AuthException(AuthErrorCodes errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.getCode();
    }

    public AuthException(AuthErrorCodes errorCode, String additionalDetail) {
        super(errorCode.getMessage() + ": " + additionalDetail);
        this.errorCode = errorCode.getCode();
    }

    public String getErrorCode() {
        return errorCode;
    }
}