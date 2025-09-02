package com.cashbox.moneywallet.authservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
}
