package com.ColdPitch.exception;

import com.ColdPitch.exception.handler.ErrorCode;
import lombok.Getter;

@Getter
public class LikeAlreadySelectedException extends RuntimeException{
    private final ErrorCode errorCode;
    public LikeAlreadySelectedException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
