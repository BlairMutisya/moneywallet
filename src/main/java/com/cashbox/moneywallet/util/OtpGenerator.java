package com.cashbox.moneywallet.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class OtpGenerator {

    private final SecureRandom random = new SecureRandom();

    public String generate6DigitOtp() {
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}
