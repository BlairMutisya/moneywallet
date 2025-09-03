package com.cashbox.moneywallet.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String phone;
    private String password;
    private String deviceId;
}