package com.cashbox.moneywallet.dto.request;

import com.cashbox.moneywallet.enums.AccountType;
import lombok.Data;

@Data
public class RegisterRequest {
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private AccountType accountType;
}
