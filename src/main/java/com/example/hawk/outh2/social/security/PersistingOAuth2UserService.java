package com.example.hawk.outh2.social.security;

import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hawk.outh2.social.user.AppUser;
import com.example.hawk.outh2.social.user.AppUserRepository;

@Service
public class PersistingOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
	private final AppUserRepository appUserRepository;

	public PersistingOAuth2UserService(AppUserRepository appUserRepository) {
		this.appUserRepository = appUserRepository;
	}

	@Override
	@Transactional
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oauth2User = delegate.loadUser(userRequest);
		UserProfile userProfile = extractUserProfile(
				userRequest.getClientRegistration().getRegistrationId(),
				oauth2User.getAttributes()
		);

		AppUser appUser = appUserRepository
				.findByProviderAndProviderUserId(userProfile.provider(), userProfile.providerUserId())
				.orElseGet(AppUser::new);

		appUser.setProvider(userProfile.provider());
		appUser.setProviderUserId(userProfile.providerUserId());
		appUser.setEmail(userProfile.email());
		appUser.setName(userProfile.name());
		appUser.setAvatarUrl(userProfile.avatarUrl());
		appUserRepository.save(appUser);

		return oauth2User;
	}

	private UserProfile extractUserProfile(String registrationId, Map<String, Object> attributes) {
		return switch (registrationId) {
			case "github" -> new UserProfile(
					"github",
					requiredAttribute(attributes, "id"),
					stringAttribute(attributes, "email"),
					stringAttribute(attributes, "name"),
					stringAttribute(attributes, "avatar_url")
			);
			case "google" -> new UserProfile(
					"google",
					requiredAttribute(attributes, "sub"),
					stringAttribute(attributes, "email"),
					stringAttribute(attributes, "name"),
					stringAttribute(attributes, "picture")
			);
			default -> throw new OAuth2AuthenticationException(
					"Unsupported OAuth provider: " + registrationId
			);
		};
	}

	private String requiredAttribute(Map<String, Object> attributes, String key) {
		Object value = attributes.get(key);
		if (value == null) {
			throw new OAuth2AuthenticationException("Missing required attribute: " + key);
		}
		return value.toString();
	}

	private String stringAttribute(Map<String, Object> attributes, String key) {
		Object value = attributes.get(key);
		return value == null ? null : value.toString();
	}

	private record UserProfile(
			String provider,
			String providerUserId,
			String email,
			String name,
			String avatarUrl
	) {
	}
}
