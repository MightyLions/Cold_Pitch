package com.ColdPitch.exception;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<?> handlerCustomException(CustomException e) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("errorCode", String.valueOf(e.getErrorCode().getStatus()));
        jsonObject.addProperty("errorMessage", e.getErrorCode().getMessage());
        String body = new Gson().toJson(jsonObject);

//        log.warn(e.getErrorCode().toString());

        return ResponseEntity.status(e.getErrorCode().getStatus()).body(body);
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

        return null;
    }
}
