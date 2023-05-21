package com.ColdPitch.jwt;


import com.ColdPitch.domain.entity.User;
import com.ColdPitch.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class SecurityUtil {
    private final UserService userService;
    //현재 시큐리티 컨텍스에 있는 유저정보와 권한을 준다. (이메일 아이디를 준다)

    public Optional<String> getCurrentUserEmail() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            log.info("Secutiry context의 정보가 없음");
            return Optional.empty();
        }

        String userEmail = null;
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            userEmail = springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof String) {
            userEmail = (String) authentication.getPrincipal();
        }
        return Optional.ofNullable(userEmail);
    }

    public Optional<User> getCurrentUser() {
        User userByEmail = null;
        Optional<String> currentUserEmail = getCurrentUserEmail();
        if (currentUserEmail.isPresent()) {
            log.info("current USER EMAIL : {}",currentUserEmail.get());
            userByEmail = userService.findUserByEmail(currentUserEmail.get());
        }
        return Optional.ofNullable(userByEmail);
    }
}
