package com.cashbox.AuthService.exception;


import com.cashbox.AuthService.enums.PinErrorCode;
import lombok.Getter;

@Getter
public class PinException extends RuntimeException {

    private final PinErrorCode errorCode;

    public PinException(PinErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public PinException(PinErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}

