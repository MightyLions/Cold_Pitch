package com.ColdPitch.config.security;

import com.ColdPitch.exception.ExceptionHandleFilter;
import com.ColdPitch.jwt.JwtFilter;
import com.ColdPitch.jwt.TokenProvider;
import com.ColdPitch.jwt.exception.JwtAccessDeniedHandler;
import com.ColdPitch.jwt.exception.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {
    private final TokenProvider tokenUtils;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final ExceptionHandleFilter exceptionHandleFilter;
    private final CorsFilter corsFilter;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .antMatchers("/v3/api-docs")
                .antMatchers("/swagger-resources/**")
                .antMatchers("/swagger-ui/**")
                .antMatchers("/webjars/**")
                .antMatchers("/swagger/**")
                .antMatchers("/api-docs/**")
                .antMatchers("/swagger-ui/**")
                ;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
        // csrf 설정 disable
        // header 및 frame disable
        security.csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(exceptionHandleFilter, UsernamePasswordAuthenticationFilter.class);

        // 세션정책 대상 전체(세션을 사용하지 않는다)
        security.sessionManagement(sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // request 정책
        security.authorizeHttpRequests()
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers("/api/v1/user/**", "/api/v1/auth/**").permitAll()
//                .antMatchers("/**").permitAll()
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
