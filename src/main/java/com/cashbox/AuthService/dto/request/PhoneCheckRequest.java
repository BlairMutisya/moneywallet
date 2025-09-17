package com.cashbox.AuthService.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PhoneCheckRequest {
    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^\\+[1-9]\\d{1,14}$",
            message = "Phone number must include country code in E.164 format (e.g., +1234567890)"
    )
    private String phone;
}