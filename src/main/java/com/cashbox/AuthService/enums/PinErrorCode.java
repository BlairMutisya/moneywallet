package com.cashbox.AuthService.enums;

public enum PinErrorCode {
    INVALID,           // Wrong PIN entered
    EXPIRED,           // PIN expired
    ATTEMPTS_EXCEEDED, // Too many attempts
    LOCKED      // Locked
}
