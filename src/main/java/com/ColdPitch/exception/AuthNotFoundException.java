package com.ColdPitch.exception;

import com.ColdPitch.exception.handler.ErrorCode;
import lombok.Getter;

@Getter
public class AuthNotFoundException extends RuntimeException{
    private final ErrorCode errorCode;
    public AuthNotFoundException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
