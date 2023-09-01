package com.example.oauth2proxy.swagger.api;

import com.example.oauth2proxy.configuration.JwkConfigurator;
import com.example.oauth2proxy.constant.JwtExtendedClaimNames;
import com.example.oauth2proxy.model.UserExample;
import com.example.oauth2proxy.security.oauth2.TokenProvider;
import com.example.oauth2proxy.swagger.model.AuthenticationResponse;
import com.example.oauth2proxy.swagger.model.LoginRequest;
import com.nimbusds.jose.HeaderParameterNames;
import com.nimbusds.jose.JWSAlgorithm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginApiControllerTest {

    @InjectMocks
    LoginApiController loginApiController;

    @InjectMocks
    JwkConfigurator jwkConfigurator = spy(JwkConfigurator.class);

    @InjectMocks
    TokenProvider tokenProvider = spy(TokenProvider.class);

    @Mock
    JwtEncoder jwtEncoder;

    @Mock
    NativeWebRequest request;

    @Mock
    AuthenticationManager authenticationManager;

    @Test
    void getRequest() {
        assertTrue(loginApiController.getRequest().isPresent());
    }

    @Test
    void authenticateUser() {
        Authentication authentication = mock(Authentication.class);
        OAuth2AuthenticatedPrincipal principal = mock(OAuth2AuthenticatedPrincipal.class);

        when(principal.getAttributes()).thenReturn(Map.of(JwtExtendedClaimNames.EMAIL, UserExample.Email));
        when(authentication.getPrincipal()).thenReturn(principal);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtEncoder.encode(any())).thenReturn(
                Jwt.withTokenValue(UserExample.JwtToken)
                        .header(HeaderParameterNames.ALGORITHM, JWSAlgorithm.HS256)
                        .claim(JwtExtendedClaimNames.EMAIL, UserExample.Email)
                        .build()
        );

        ResponseEntity<AuthenticationResponse> responseEntity = loginApiController.authenticateUser(
                new LoginRequest()
                        .email(UserExample.Email)
                        .password(UserExample.Password)
        );
        assertTrue(HttpStatus.OK.equals(responseEntity.getStatusCode()));
    }
}