package com.example.oauth2proxy.model;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

public enum AuthorizationProvider {
    INTERNAL("internal"),
    FACEBOOK("facebook"),
    GOOGLE("google"),
    GITHUB("github");

    private String registrationId;

    AuthorizationProvider(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getRegistrationId() {
        return this.registrationId;
    }

    public String getImplementationClassName() {
        return String.format("%1$sOAuth2UserInfo", StringUtils.capitalize(registrationId));
    }

    public static AuthorizationProvider fromEnumeration(String registrationId) {
        Optional<AuthorizationProvider> oAuth2ProviderOptional = Arrays.stream(AuthorizationProvider.values()).filter(provider -> provider.registrationId.equalsIgnoreCase(registrationId)).findFirst();
        return oAuth2ProviderOptional.isPresent() ? oAuth2ProviderOptional.get() : null;
    }
}
