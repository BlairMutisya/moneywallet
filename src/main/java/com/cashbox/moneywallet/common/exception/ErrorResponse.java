package com.cashbox.moneywallet.common.exception;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ErrorResponse {
    private int status;
    private String type;  // Changed from 'error' to 'type'
    private String message;
    private String path;
    private Instant timestamp;
}