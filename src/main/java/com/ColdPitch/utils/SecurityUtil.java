package com.ColdPitch.utils;


import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class SecurityUtil {
    //현재 시큐리티 컨텍스에 있는 유저정보와 권한을 준다. (이메일 아이디를 준다)

    public static Optional<String> getCurrentUserEmail() {
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

        log.info(userEmail);
        return Optional.ofNullable(userEmail);
    }

    /**
     * 현재 SecurityContext에 있는 유저의 권한 컬렉션을 리턴
     *
     * @return
     * UserDetails
     * 유저의 권한 컬렉션을 리턴
     */
    public static Optional<Collection<? extends GrantedAuthority>> getCurrentUserAuthorities() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            log.info("Secutiry context의 정보가 없음");
            return Optional.empty();
        }

        return Optional.of(authentication.getAuthorities());
    }

    /**
     * 현재 주어진 role과 SecurityContext에 있는 유저를 비교하여 Boolean값 리턴
     * @param role
     * 유저의 역할
     * @return
     * 동일할 경우 true, 그외의 경우에는 false
     */
    public static boolean checkCurrentUserRole(String role) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            log.info("Secutiry context의 정보가 없음");
            return false;
        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals(role)) {
                return true;
            }
        }

        return false;
    }
}
