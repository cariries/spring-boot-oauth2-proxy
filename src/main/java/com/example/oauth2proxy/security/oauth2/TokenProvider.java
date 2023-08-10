package com.example.oauth2proxy.security.oauth2;

import com.example.oauth2proxy.security.oauth2.user.AuthorizedUserInfo;
import com.example.oauth2proxy.security.oauth2.user.AuthorizedUserInfoFactory;
import com.example.oauth2proxy.configuration.JwkConfigurator;
import com.example.oauth2proxy.model.AuthorizationProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

@Service
@Slf4j
public class TokenProvider {

    @Autowired
    JwkConfigurator jwkConfigurator;

    @Autowired
    JwtEncoder jwtEncoder;

    @Autowired
    JwtDecoder jwtDecoder;

    public String generateToken(final Authentication authentication) {
        return generateToken(authentication, null);
    }

    public String generateToken(final Authentication authentication, final AuthorizationProvider authorizationProvider) {
        OAuth2AuthenticatedPrincipal userPrincipal = (OAuth2AuthenticatedPrincipal) authentication.getPrincipal();
        AuthorizedUserInfo authorizedUserInfo =
                authorizationProvider != null ?
                        AuthorizedUserInfoFactory.newInstance(authorizationProvider.getRegistrationId(), userPrincipal.getAttributes()) :
                        AuthorizedUserInfoFactory.newInstance(userPrincipal.getAttributes());

        Instant instantNow = Instant.now();
        Jwt jwt = jwtEncoder.encode(
                JwtEncoderParameters.from(
                        JwtClaimsSet.builder()
                                .subject(authorizedUserInfo.getEmail())
                                .issuedAt(instantNow)
                                .expiresAt(instantNow.plusMillis(jwkConfigurator.getTokenExpirationInMs()))
                                .claims(claims -> claims.putAll(Optional.ofNullable(authorizedUserInfo.getClaims()).orElse(Collections.emptyMap())))
                                .build()
                )
        );
        return jwt.getTokenValue();
    }

    public boolean validateToken(String tokenValue) {
        boolean validatedSuccess = false;
        try {
            // Validate whether token can be decoded
            jwtDecoder.decode(tokenValue);
            validatedSuccess = true;
        } catch (JwtException jwtException) {
            log.warn("Invalid Jwt Token Found", jwtException);
        }
        return validatedSuccess;
    }

    public String getSubjectFromToken(String tokenValue) {
        Jwt token = jwtDecoder.decode(tokenValue);
        return token.getSubject();
    }
}