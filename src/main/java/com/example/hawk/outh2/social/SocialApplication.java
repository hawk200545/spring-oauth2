package com.example.hawk.outh2.social;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SocialApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialApplication.class, args);
	}

	@Bean
	public ApplicationRunner applicationRunner(
			@Value("${GITHUB_CLIENT_ID}") String githubClientId,
			@Value("${GOOGLE_CLIENT_ID}") String googleClientId) {
		return args -> {
			System.out.println("GitHub Client ID: " + githubClientId);
			System.out.println("Google Client ID: " + googleClientId);
			if (githubClientId == null || githubClientId.isEmpty() || githubClientId.startsWith("${")) {
				System.out.println("ERROR: Dotenv not working - properties not resolved!");
			} else {
				System.out.println("SUCCESS: Dotenv working correctly!");
			}
		};
	}
}
