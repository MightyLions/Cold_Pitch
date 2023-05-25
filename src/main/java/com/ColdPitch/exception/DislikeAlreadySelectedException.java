package com.ColdPitch.exception;

import com.ColdPitch.exception.handler.ErrorCode;
import lombok.Getter;

@Getter
public class DislikeAlreadySelectedException extends RuntimeException{
    private final ErrorCode errorCode;
    public DislikeAlreadySelectedException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
