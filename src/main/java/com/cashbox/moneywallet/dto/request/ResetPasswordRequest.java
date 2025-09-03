package com.cashbox.moneywallet.dto.request;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String email;
    private String otp;
    private String newPassword;
}
