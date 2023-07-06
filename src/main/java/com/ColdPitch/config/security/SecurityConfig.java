package com.ColdPitch.config.security;

import com.ColdPitch.jwt.JwtFilter;
import com.ColdPitch.jwt.exception.JwtAccessDeniedHandler;
import com.ColdPitch.jwt.exception.JwtAuthenticationEntryPoint;
import com.ColdPitch.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {
    private final TokenProvider tokenUtils;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception{
        // csrf 설정 disable
        // header 및 frame disable
        security.csrf()
                .disable()
                .headers()
                .frameOptions()
                .disable();

        // 세션정책 대상 전체
        security.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS);

        // request 정책
        security.authorizeHttpRequests()
                .antMatchers("/swagger").permitAll()
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated();

        // login disable
        security.formLogin().disable();

        // http disable
        security.httpBasic().disable();

        // jwt filter
        security.addFilterBefore(new JwtFilter(tokenUtils), UsernamePasswordAuthenticationFilter.class);

        //jwt exception handling
        security.exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler);

        return security.build();
    }
}
