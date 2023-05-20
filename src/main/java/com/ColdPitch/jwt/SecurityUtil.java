package com.ColdPitch.jwt;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@Slf4j
public class SecurityUtil {

    //현재 시큐리티 컨텍스에 있는 유저정보와 권한을 준다.
    public static Optional<String> getCurrentUserLoginId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            log.info("Secutiry context의 정보가 없음");
            return Optional.empty();
        }

        String userLoginId = null;
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            userLoginId = springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof String) {
            userLoginId = (String) authentication.getPrincipal();
        }
        return Optional.ofNullable(userLoginId);
    }
}
