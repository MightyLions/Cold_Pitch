package com.ColdPitch.exception;

import com.ColdPitch.exception.handler.ErrorCode;

public class CustomSecurityException extends RuntimeException {

    private ErrorCode errorCode;

    public CustomSecurityException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public CustomSecurityException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }
}