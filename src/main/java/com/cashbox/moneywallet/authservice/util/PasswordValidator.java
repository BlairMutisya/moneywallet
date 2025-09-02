package com.cashbox.moneywallet.authservice.util;

import com.cashbox.moneywallet.authservice.exception.AuthErrorCodes;
import com.cashbox.moneywallet.authservice.exception.AuthException;

public class PasswordValidator {
    // Configuration constants
    private static final int MIN_LENGTH = 12;
    private static final int MAX_LENGTH = 128;
    private static final String SPECIAL_CHARS = "!@#$%^&*()_+-=[]{}|;:'\",.<>?/";
    private static final int MIN_COMPLEXITY_CATEGORIES = 3; // Uppercase, lowercase, digit, special

    public static void validate(String password) {
        if (password == null || password.isBlank()) {
            throw new AuthException(AuthErrorCodes.INVALID_PASSWORD,
                    "Password cannot be empty");
        }

        if (password.length() < MIN_LENGTH) {
            throw new AuthException(AuthErrorCodes.INVALID_PASSWORD,
                    String.format("Password must be at least %d characters", MIN_LENGTH));
        }

        if (password.length() > MAX_LENGTH) {
            throw new AuthException(AuthErrorCodes.INVALID_PASSWORD,
                    String.format("Password cannot exceed %d characters", MAX_LENGTH));
        }

        validateComplexity(password);
    }

    private static void validateComplexity(String password) {
        int complexityScore = 0;

        // Check for uppercase letters
        if (password.matches(".*[A-Z].*")) {
            complexityScore++;
        }

        // Check for lowercase letters
        if (password.matches(".*[a-z].*")) {
            complexityScore++;
        }

        // Check for digits
        if (password.matches(".*\\d.*")) {
            complexityScore++;
        }

        // Check for special characters
        if (password.chars().anyMatch(ch -> SPECIAL_CHARS.indexOf(ch) >= 0)) {
            complexityScore++;
        }

        if (complexityScore < MIN_COMPLEXITY_CATEGORIES) {
            throw new AuthException(AuthErrorCodes.INVALID_PASSWORD,
                    String.format(
                            "Password must contain at least %d of: uppercase, lowercase, digits, or special characters (%s)",
                            MIN_COMPLEXITY_CATEGORIES,
                            SPECIAL_CHARS
                    ));
        }

        // Check for common patterns
        if (password.matches("(.)\\1{2,}")) { // 3+ repeating chars
            throw new AuthException(AuthErrorCodes.INVALID_PASSWORD,
                    "Password contains too many repeating characters");
        }

        if (password.matches(".*(123|abc|qwerty|password).*")) {
            throw new AuthException(AuthErrorCodes.INVALID_PASSWORD,
                    "Password contains common weak patterns");
        }
    }
}