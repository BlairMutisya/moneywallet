package com.cashbox.moneywallet.common;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public static <T> BaseApiResponse<T> success(T data, String message) {
        return BaseApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> BaseApiResponse<T> failure(String message) {
        return BaseApiResponse.<T>builder()
                .success(false)
                .message(message)
                .build();
    }
}


