package com.cashbox.AuthService.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response from /api/v1/pin/verify endpoint.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PinVerificationResponse {
    private boolean valid;
}
