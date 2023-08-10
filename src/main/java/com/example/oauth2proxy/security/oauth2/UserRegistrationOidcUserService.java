package com.example.oauth2proxy.security.oauth2;

import com.example.oauth2proxy.service.OAuth2UserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationOidcUserService extends OidcUserService {

    @Autowired
    OAuth2UserRegistrationService oAuth2UserRegistrationService;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        oAuth2UserRegistrationService.processOAuth2UserRequest(userRequest, oidcUser);
        return oidcUser;
    }
}