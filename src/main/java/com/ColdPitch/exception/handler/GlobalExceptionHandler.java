package com.ColdPitch.exception.handler;

import com.ColdPitch.exception.CustomException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<?> handlerCustomException(CustomException e) {
        log.error("Exception: " + e.getErrorCode().getMessage());
        ErrorResponse response = new ErrorResponse(e.getErrorCode());
        return new ResponseEntity<> (response, e.getErrorCode().getStatus());
    }

    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<?> handlerNoSuchElementException(NoSuchElementException e) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("message", e.getMessage());
        String body = new Gson().toJson(jsonObject);

        return ResponseEntity.status(204).body(body);
    }


    @ExceptionHandler(Exception.class)
    protected ResponseEntity<?> handlerException(Exception e) {
        log.error("Exception : " + e.getMessage());
        return ResponseEntity.status(500).body("에러코드 정의해줘...");
    }
}
