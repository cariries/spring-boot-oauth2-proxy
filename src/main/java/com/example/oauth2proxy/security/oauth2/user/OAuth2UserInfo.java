package com.example.oauth2proxy.security.oauth2.user;

import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public abstract class OAuth2UserInfo implements AuthorizedUserInfo {

    private Map<String, Object> attributes;

    public Map<String, Object> getAttributes() {
        return Optional.ofNullable(attributes).orElse(new HashMap<>());
    }

    public Map<String, Object> getClaims() {
        return Collections.emptyMap();
    }
}