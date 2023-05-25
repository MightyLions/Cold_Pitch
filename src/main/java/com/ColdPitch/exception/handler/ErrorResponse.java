package com.ColdPitch.exception.handler;

import com.ColdPitch.exception.handler.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private int status;
    private String code;
    private String message;
    private ErrorCode errorCode;
    private HttpStatus httpStatus;

    public ErrorResponse(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.httpStatus = errorCode.getStatus();
        this.message = errorCode.getMessage();
    }
}
