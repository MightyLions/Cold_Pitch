package com.ColdPitch.exception;

import com.ColdPitch.exception.handler.ErrorCode;
import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException{
    private final ErrorCode errorCode;
    public UserNotFoundException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
