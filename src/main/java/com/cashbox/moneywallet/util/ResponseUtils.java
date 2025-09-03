package com.cashbox.moneywallet.util;

import com.cashbox.moneywallet.common.BaseApiResponse;

public class ResponseUtils {

    public static <T> BaseApiResponse<T> success(T data, String message) {
        return new BaseApiResponse<>(true, message, data);
    }

    public static <T> BaseApiResponse<T> error(String message) {
        return new BaseApiResponse<>(false, message, null);
    }
}
