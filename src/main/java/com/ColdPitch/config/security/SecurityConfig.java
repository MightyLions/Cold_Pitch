package com.ColdPitch.config.security;

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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {
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

        return security.build();
    }
}
