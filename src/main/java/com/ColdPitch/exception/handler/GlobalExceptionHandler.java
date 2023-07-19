package com.ColdPitch.exception.handler;

import com.ColdPitch.exception.CustomException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<?> handlerCustomException(CustomException e) {
        log.error("Exception: " + e.getErrorCode().getMessage());
        ErrorResponse response = new ErrorResponse(e.getErrorCode());
        return new ResponseEntity<>(response, e.getErrorCode().getStatus());
    }

    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<?> handlerNoSuchElementException(NoSuchElementException e) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("message", e.getMessage());
        String body = new Gson().toJson(jsonObject);

        return ResponseEntity.status(204).body(body);
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<?> handlerLogin(NoSuchElementException e) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("message", e.getMessage());
        String body = new Gson().toJson(jsonObject);

        return ResponseEntity.status(400).body(body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<?> handlerConstraintViolationException(ConstraintViolationException e) {
        Map<Object, String> validExceptions = new HashMap<>();
        e.getConstraintViolations().forEach(
                error -> {
                    PathImpl path = (PathImpl) error.getPropertyPath();
                    validExceptions.put(path.getLeafNode().getName(), error.getMessage());
                }
        );

        return ResponseEntity.badRequest().body(validExceptions);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> validExceptions = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(
                error -> {
                    validExceptions.put(error.getField(), error.getDefaultMessage());
                }
        );

        return ResponseEntity.badRequest().body(validExceptions);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<?> handlerException(Exception e) {
        log.error("Exception : " + e.getMessage());
        return ResponseEntity.status(500).body("에러코드 정의해줘..." + e.getMessage());
    }
}
