package com.cashbox.AuthService.service;

import com.cashbox.AuthService.dto.response.PinCreateResponse;

public interface PinService {
    PinCreateResponse setPin(Long userId, String pin);
    void changePin(Long userId, String oldPin, String newPin);
    boolean verifyPin( String pin); // returns true/false, internal lock tracking
}
