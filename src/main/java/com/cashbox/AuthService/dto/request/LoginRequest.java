package com.cashbox.AuthService.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String phone;
    private String password;
//    private String deviceId;
}