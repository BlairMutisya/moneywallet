package com.cashbox.moneywallet.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseApiResponse<T> {
    private String status;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public static <T> BaseApiResponse<T> success(String message, T data) {
        return BaseApiResponse.<T>builder()
                .status("success")
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> BaseApiResponse<T> failure(String message) {
        return BaseApiResponse.<T>builder()
                .status("failure")
                .message(message)
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
