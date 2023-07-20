package com.ColdPitch.jwt.exception;


import com.ColdPitch.exception.CustomSecurityException;
import com.ColdPitch.exception.handler.ErrorCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, IOException {
        //권한 없이 접근 하려 하는 경우 403
        throw new CustomSecurityException(ErrorCode.NO_AUTHORIZATION_TOKEN);
    }
}
