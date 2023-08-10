package com.example.oauth2proxy.service;

import com.example.oauth2proxy.model.AuthorizationProvider;
import com.example.oauth2proxy.security.oauth2.user.AuthorizedUserInfo;
import com.example.oauth2proxy.security.oauth2.user.AuthorizedUserInfoFactory;
import com.example.oauth2proxy.exception.OAuth2AuthenticationProcessingException;
import com.example.oauth2proxy.jpa.entity.User;
import com.example.oauth2proxy.jpa.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
public class OAuth2UserRegistrationService {
    @Autowired
    UserRepository userRepository;

    public <R extends OAuth2UserRequest, T extends OAuth2AuthenticatedPrincipal>
    void processOAuth2UserRequest(R userRequest, T userPrincipal) {

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        AuthorizedUserInfo authorizedUserInfo = AuthorizedUserInfoFactory.newInstance(registrationId, userPrincipal.getAttributes());

        if (StringUtils.isBlank(authorizedUserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<User> userOptional = userRepository.findByEmail(authorizedUserInfo.getEmail());
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            if (!user.getProvider().equals(AuthorizationProvider.fromEnumeration(registrationId))) {
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        user.getProvider() + " account. Please use your " + user.getProvider() +
                        " account to login.");
            }
            user = updateExistingUser(user, authorizedUserInfo);
            log.debug("Updated Existing User Profile (Email: {}, Provider: {})", user.getEmail(), user.getProvider());
        } else {
            user = registerNewUser(userRequest, authorizedUserInfo);
            log.debug("Registered New User Profile (Email: {}, Provider: {})", user.getEmail(), user.getProvider());
        }
    }

    private <R extends OAuth2UserRequest> User registerNewUser(R userRequest, AuthorizedUserInfo userInfo) {
        User user = new User();
        user.setProvider(AuthorizationProvider.fromEnumeration(userRequest.getClientRegistration().getRegistrationId()));
        user.setProviderId(userInfo.getId());
        user.setName(userInfo.getName());
        user.setEmail(userInfo.getEmail());
        user.setImageUrl(userInfo.getImageUrl());
        return userRepository.save(user);
    }

    private User updateExistingUser(User existingUser, AuthorizedUserInfo authorizedUserInfo) {
        existingUser.setName(authorizedUserInfo.getName());
        existingUser.setImageUrl(authorizedUserInfo.getImageUrl());
        return userRepository.save(existingUser);
    }
}