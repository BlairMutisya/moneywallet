package com.cashbox.moneywallet.authservice.dto.request;

import lombok.Data;

@Data
public class RegisterRequest {
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private String accountType;
}
