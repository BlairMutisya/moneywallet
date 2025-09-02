package com.cashbox.moneywallet.authservice.exception;

public enum AuthErrorCodes {
    // Format: CODE("Error message")
    INVALID_CREDENTIALS("AUTH_001", "Invalid email or password"),
    ACCOUNT_DISABLED("AUTH_002", "Account is disabled"),
    ACCOUNT_LOCKED("AUTH_003", "Account locked due to multiple failed attempts"),
    EXPIRED_TOKEN("AUTH_004", "Session expired. Please log in again"),
    UNAUTHORIZED("AUTH_005", "Authentication required"),
    EMAIL_EXISTS("AUTH_006", "Email already registered"),
    INVALID_PASSWORD("AUTH_007", "Invalid password requirements");

    private final String code;
    private final String message;

    AuthErrorCodes(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}