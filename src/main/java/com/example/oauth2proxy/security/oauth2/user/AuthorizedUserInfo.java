package com.example.oauth2proxy.security.oauth2.user;

import java.util.Map;

public interface AuthorizedUserInfo {
    Map<String, Object> getAttributes();

    String getId();

    String getName();

    String getEmail();

    String getImageUrl();

    Map<String, Object> getClaims();
}