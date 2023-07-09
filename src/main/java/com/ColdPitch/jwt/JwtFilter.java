package com.ColdPitch.jwt;


import com.ColdPitch.config.security.JwtConfig;
import com.ColdPitch.domain.entity.dto.jwt.RefreshToken;
import com.ColdPitch.domain.entity.dto.jwt.TokenDto;
import com.ColdPitch.domain.repository.RefreshTokenRepository;
import com.ColdPitch.exception.CustomException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.ColdPitch.exception.handler.ErrorCode.USER_INVALID_USER_REFRESH_TOKEN;
import static com.ColdPitch.exception.handler.ErrorCode.USER_NOT_ACTIVE;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends GenericFilter {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;
    private final JwtConfig jwtConfig;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String jwt = resolveToken((HttpServletRequest) request);
        String requestURI = httpServletRequest.getRequestURI();
        log.info("accessToken {}", jwt);
        try {
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                Authentication authentication = tokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("인증 정보를 저장했습니다 {}, uri: {}, role : {}", authentication.getName(), requestURI, authentication.getAuthorities());
            } else {
                log.info("access token 이 비워져 있습니다.");
            }
        } catch (ExpiredJwtException e) {
            log.info("만료된 Access 토큰입니다.");
            //refresh token을 확인한다.

            String refreshToken = resolveCookie((HttpServletRequest) request);
            if (StringUtils.hasText(refreshToken) && tokenProvider.validateToken(refreshToken)) {

                Authentication authentication = tokenProvider.getAuthentication(jwt);
                RefreshToken findRefreshToken = refreshTokenRepository.findByKey(authentication.getName()).orElseThrow(() -> new CustomException(USER_NOT_ACTIVE));

                log.info("{}", findRefreshToken);
                if (!findRefreshToken.getValue().equals(refreshToken)) {
                    throw new CustomException(USER_INVALID_USER_REFRESH_TOKEN);
                }

                //새로운 토큰 발급
                TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
                log.debug("TEST REFRESH {}", tokenDto.getRefreshToken());
                log.debug("TEST ACCESS {}", tokenDto.getAccessToken());
                RefreshToken newRefreshToken = findRefreshToken.updateValue(tokenDto.getRefreshToken()); //RTR 새로운 리프레시 토
                refreshTokenRepository.save(newRefreshToken);

                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                httpServletResponse.setHeader(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + tokenDto.getAccessToken());
                String cookie = "refreshToken=" + tokenDto.getRefreshToken() + "; Path=/; Max-Age=" + jwtConfig.getRefreshExpirationTime() + "; HttpOnly; readonly";
                httpServletResponse.addHeader("Set-Cookie", cookie);

                Authentication newAuthentication = tokenProvider.getAuthentication(tokenDto.getAccessToken());
                SecurityContextHolder.getContext().setAuthentication(newAuthentication);
                log.info("인증 정보를 새로 발급하여 저장했습니다 {}, uri: {}, role : {}", newAuthentication.getName(), requestURI, newAuthentication.getAuthorities());
            } else {
                log.info("refresh token이 비워져 있습니다.");
            }
        }

        chain.doFilter(request, response);
    }


    //token 분리
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.split(" ")[1].trim();
        }
        return null;
    }


    private String resolveCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("refreshToken")) {
                return cookie.getValue();
            }
        }
        return null;
    }
}

//eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdHJpbmdAZW1haWwuY29tIiwiYXV0aCI6IkFETUlOIiwiZXhwIjoxNjg4OTExMzY0fQ.aCh202AWLfxELsWThcBBTlIfdw4HvLCHEpKDFqbXu5Yl3g8_rPAHh_vTVmZUqebVCN-mU2ua5z28MlHRjkLW-g
//eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdHJpbmdAZW1haWwuY29tIiwiYXV0aCI6IkFETUlOIiwiZXhwIjoxNjg4OTExMzc3fQ.cQY-yD0Zs2RjOaSUvRHQfMGbUyyPpRJeF7nRRmzu9zX0HpG8gZhQdKcf0FMDEkLYZSOdUPwElQqKjmCBoep6_A