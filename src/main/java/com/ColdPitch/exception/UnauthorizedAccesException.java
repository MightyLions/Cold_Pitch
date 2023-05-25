package com.ColdPitch.exception;

import com.ColdPitch.exception.handler.ErrorCode;
import lombok.Getter;

@Getter
public class UnauthorizedAccesException extends RuntimeException{
    private final ErrorCode errorCode;
    public UnauthorizedAccesException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
