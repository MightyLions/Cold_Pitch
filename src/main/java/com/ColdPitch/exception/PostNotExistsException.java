package com.ColdPitch.exception;

import com.ColdPitch.exception.handler.ErrorCode;
import lombok.Getter;

@Getter
public class PostNotExistsException extends RuntimeException{
    private final ErrorCode errorCode;
    public PostNotExistsException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
