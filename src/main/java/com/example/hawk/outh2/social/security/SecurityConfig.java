package com.example.hawk.outh2.social.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.AbstractRequestMatcherRegistry;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.
                csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req-> {
                    req.anyRequest().authenticated();
                })
                .oauth2Login(oauth->
                        oauth.defaultSuccessUrl("/demo",true)
                                .permitAll()
                );

        return httpSecurity.build();
    }
}
