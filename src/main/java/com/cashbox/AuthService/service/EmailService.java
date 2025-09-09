package com.cashbox.AuthService.service;

public interface EmailService {
    void sendOtp(String to, String otp);
}
