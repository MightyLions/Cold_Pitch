package com.ColdPitch.jwt;

import com.ColdPitch.config.security.JwtConfig;
import com.ColdPitch.domain.entity.dto.jwt.TokenDto;
import com.ColdPitch.exception.CustomException;
import com.ColdPitch.exception.CustomSecurityException;
import com.ColdPitch.exception.handler.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;


@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider implements InitializingBean {
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "Bearer";

    private final JwtConfig jwtConfig;
    private Key key;

    //실제 로그인시 발급하는 토큰
    public TokenDto generateTokenDto(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        long now = new Date().getTime();

        //access token 생성
        Date accessTokenExpiresIn = new Date(now + jwtConfig.getAccessExpirationTime());
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName()) //payload
                .claim(AUTHORITIES_KEY, authorities) //payload
                .setExpiration(accessTokenExpiresIn)//payload
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
        //refresh token 생성
        Date refreshTokenExpiresIn = new Date(now + jwtConfig.getRefreshExpirationTime());
        String refreshToken = Jwts.builder()
                .setExpiration(refreshTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        //권한이 있는 경우
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString()
                        .split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, accessToken, authorities);
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().
                    setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    //토큰의 유효성검사
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true; //문제 없음
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
            throw new CustomSecurityException(ErrorCode.INVALID_JWT_AUTHORIZATION);
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
            throw new CustomSecurityException(ErrorCode.UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
            throw new CustomSecurityException(ErrorCode.INVALID_JWT_TOKEN);
        }
    }

    @Override
    public void afterPropertiesSet() {
        byte[] decode = Decoders.BASE64.decode(jwtConfig.getSecretKey());
        this.key = Keys.hmacShaKeyFor(decode);
    }
}
