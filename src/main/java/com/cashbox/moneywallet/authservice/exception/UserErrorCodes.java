package com.cashbox.moneywallet.authservice.exception;

public enum UserErrorCodes {
    // Validation Errors (4xx)
    USER_NOT_FOUND("USER_001", "User not found"),
    INVALID_USER_ID("USER_002", "Invalid user ID format"),
    EMAIL_ALREADY_EXISTS("USER_003", "Email address already registered"),
    INVALID_EMAIL_FORMAT("USER_004", "Invalid email format"),
    PASSWORD_COMPLEXITY_FAILED("USER_005", "Password doesn't meet complexity requirements"),
    CURRENT_PASSWORD_MISMATCH("USER_006", "Current password doesn't match"),
    PASSWORD_REUSE_NOT_ALLOWED("USER_007", "Cannot reuse previous passwords"),
    PROFILE_UPDATE_CONFLICT("USER_008", "Concurrent profile modification detected"),

    // Business Logic Errors (4xx)
    SELF_ROLE_MODIFICATION("USER_010", "Cannot modify your own role"),
    SELF_STATUS_MODIFICATION("USER_011", "Cannot disable your own account"),
    DEMOTE_LAST_ADMIN("USER_012", "Cannot demote the last admin"),
    KYC_REQUIRED("USER_013", "KYC verification required for this action"),

    // Security Errors (4xx)
    UNAUTHORIZED_PROFILE_ACCESS("USER_020", "Not authorized to access this profile"),

    // System Errors (5xx)
    USER_PROFILE_UPDATE_FAILED("USER_100", "Failed to update user profile"),
    AVATAR_UPLOAD_FAILED("USER_101", "Failed to upload profile image");

    private final String code;
    private final String message;

    UserErrorCodes(String code, String message) {
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