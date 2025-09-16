package com.cashbox.AuthService.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PinChangeRequest {
    @NotNull
    private Long userId;

    @NotBlank
    @Pattern(regexp = "\\d{6}", message = "Old PIN must be exactly 6 digits")
    private String oldPin;

    @NotBlank
    @Pattern(regexp = "\\d{6}", message = "New PIN must be exactly 6 digits")
    private String newPin;
}
