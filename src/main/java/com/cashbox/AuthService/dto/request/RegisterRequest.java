package com.cashbox.AuthService.dto.request;

import lombok.Data;

@Data
public class RegisterRequest {
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private Long accountTypeId;
}
