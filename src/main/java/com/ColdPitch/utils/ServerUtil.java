package com.ColdPitch.utils;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class ServerUtil {
    public static String getCurrentBaseUrl() {
        return ServletUriComponentsBuilder
            .fromCurrentContextPath()
            .build()
            .toUriString();
    }

    public static String getRequestURL() {
        HttpServletRequest request =
            ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getRequestURL().toString();
    }

    public static int getRequestPort() {
        HttpServletRequest request =
            ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getServerPort();
    }
}
