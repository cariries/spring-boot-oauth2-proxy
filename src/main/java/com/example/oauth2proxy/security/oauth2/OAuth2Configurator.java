package com.example.oauth2proxy.security.oauth2;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OAuth2Configurator {
    private List<String> authorizedRedirectUris;
    private String authorizationEndpointBaseUri;
    private String redirectionEndpointBaseUri;
}