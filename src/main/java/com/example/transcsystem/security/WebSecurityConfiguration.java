package com.example.transcsystem.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Samwel Wafula
 * Created on July 09, 2023.
 * Time 1:00 PM
 */
@Configuration
@EnableMethodSecurity(jsr250Enabled = true)
public class WebSecurityConfiguration {

    @Autowired
    private CustomOncePreRequestFilter requestFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .cors()
                .and()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.OPTIONS,"*")
                .permitAll()
                .requestMatchers( "/resources/**","/v3/**", "/get-token", "/api/v1/registration","/api/v1/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic()
                .and()
                .headers()
                .frameOptions()
                .disable()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);

        http.addFilterAfter(
                requestFilter,
                UsernamePasswordAuthenticationFilter.class
        );


        return http.build();

    }
}
