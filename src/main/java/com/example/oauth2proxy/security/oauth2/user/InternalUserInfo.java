package com.example.oauth2proxy.security.oauth2.user;

import com.example.oauth2proxy.constant.JwtExtendedClaimNames;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InternalUserInfo implements AuthorizedUserInfo {

    private Map<String, Object> attributes;

    public InternalUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getId() {
        return (String) getAttributes().get(JwtExtendedClaimNames.OID);
    }

    @Override
    public String getName() {
        return (String) getAttributes().get(JwtExtendedClaimNames.NAME);
    }

    @Override
    public String getEmail() {
        return (String) getAttributes().get(JwtExtendedClaimNames.EMAIL);
    }

    @Override
    public String getImageUrl() {
        return (String) getAttributes().get(JwtExtendedClaimNames.AVATAR);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Optional.ofNullable(attributes).orElse(new HashMap<>());
    }

    @Override
    public Map<String, Object> getClaims() {
        return Collections.emptyMap();
    }
}