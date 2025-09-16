package com.cashbox.AuthService.dto.request;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PinCreateRequest {
    @NotNull
    private Long userId;

    @NotNull
    @Pattern(regexp = "\\d{6}", message = "PIN must be exactly 6 digits")
    private String pin;
}

