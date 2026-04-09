package com.example.hawk.outh2.social.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final PersistingOAuth2UserService persistingOAuth2UserService;

	public SecurityConfig(PersistingOAuth2UserService persistingOAuth2UserService) {
		this.persistingOAuth2UserService = persistingOAuth2UserService;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(req -> req.anyRequest().authenticated())
				.oauth2Login(oauth -> oauth
						.userInfoEndpoint(userInfo -> userInfo.userService(persistingOAuth2UserService))
						.defaultSuccessUrl("/api/v1/demo", true)
						.permitAll()
				)
				.logout(logout -> logout.logoutSuccessUrl("/login?logout"));

		return httpSecurity.build();
	}
}
