package com.cashbox.AuthService.util;

import java.time.Instant;

public class TimeUtils {

    public static Instant calculateExpiryInMinutes(int minutes) {
        return Instant.now().plusSeconds(minutes * 60L);
    }
}
