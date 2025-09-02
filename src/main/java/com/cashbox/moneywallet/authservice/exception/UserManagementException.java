package com.cashbox.moneywallet.authservice.exception;

public class UserManagementException extends RuntimeException {
    private final UserErrorCodes errorCode;

    public UserManagementException(UserErrorCodes errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public UserErrorCodes getErrorCode() {
        return errorCode;
    }
}