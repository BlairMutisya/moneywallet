package com.cashbox.AuthService.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseApiResponse<T> {
    private String message;
    private T data;
    private String errorCode;
    private LocalDateTime timestamp;
    private Integer status;

    // Success static factories
    public static <T> BaseApiResponse<T> success(T data) {
        return success("Operation successful", data);
    }

    public static <T> BaseApiResponse<T> success(String message, T data) {
        BaseApiResponse<T> response = new BaseApiResponse<>();
        response.message = message;
        response.data = data;
        response.timestamp = LocalDateTime.now();
        return response;
    }

    public static BaseApiResponse<Void> success(String message) {
        BaseApiResponse<Void> response = new BaseApiResponse<>();
        response.message = message;
        response.timestamp = LocalDateTime.now();
        return response;
    }

    // Error static factories
    public static <T> BaseApiResponse<T> error(String errorCode, String message) {
        BaseApiResponse<T> response = new BaseApiResponse<>();
        response.errorCode = errorCode;
        response.message = message;
        response.timestamp = LocalDateTime.now();
        return response;
    }

    public static <T> BaseApiResponse<T> error(String errorCode, String message, T data) {
        BaseApiResponse<T> response = new BaseApiResponse<>();
        response.errorCode = errorCode;
        response.message = message;
        response.data = data;
        response.timestamp = LocalDateTime.now();
        return response;
    }
}