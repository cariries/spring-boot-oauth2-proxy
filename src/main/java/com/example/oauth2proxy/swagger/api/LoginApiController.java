package com.example.oauth2proxy.swagger.api;

import com.example.oauth2proxy.security.oauth2.TokenProvider;
import com.example.oauth2proxy.swagger.model.AuthenticationResponse;
import com.example.oauth2proxy.swagger.model.LoginRequest;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
@Controller
@RequestMapping("${openapi.oauth2-proxy.base-path:/oauth2-proxy/v1}")
public class LoginApiController implements LoginApi {

    private final NativeWebRequest request;

    @Autowired
    public LoginApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    TokenProvider tokenProvider;

    @Override
    public ResponseEntity<AuthenticationResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new AuthenticationResponse().accessToken(token));
    }
}
