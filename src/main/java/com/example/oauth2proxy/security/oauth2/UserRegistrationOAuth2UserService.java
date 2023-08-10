package com.example.oauth2proxy.security.oauth2;

import com.example.oauth2proxy.service.OAuth2UserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    OAuth2UserRegistrationService oAuth2UserRegistrationService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        oAuth2UserRegistrationService.processOAuth2UserRequest(userRequest, oAuth2User);
        return oAuth2User;
    }
}