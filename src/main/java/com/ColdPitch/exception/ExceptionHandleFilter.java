package com.ColdPitch.exception;


import com.ColdPitch.exception.handler.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 필터 예외 처리 제공부분
 */
@Slf4j
@Component
public class ExceptionHandleFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, java.io.IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (CustomSecurityException e) {
            log.info("catch Filter Exception");
            setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, response, e);
        }
    }

    public void setErrorResponse(HttpStatus status, HttpServletResponse response, CustomSecurityException e) {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ErrorCode errorCode = e.getErrorCode();

        try {
            String json = new ObjectMapper().writeValueAsString(errorCode.toString());
            response.setStatus(errorCode.getStatus().value()); //상태 코드로 변경
            response.getWriter().write(json);
        } catch (IOException e1) {
            e.printStackTrace();
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        } catch (java.io.IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
