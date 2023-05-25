package com.ColdPitch.exception;

import com.ColdPitch.exception.handler.ErrorCode;
import lombok.Getter;

@Getter
public class CommentException extends RuntimeException{
    private final ErrorCode errorCode;
    public CommentException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
