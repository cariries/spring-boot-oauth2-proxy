package com.example.oauth2proxy.swagger.api;

import com.example.oauth2proxy.exception.BadRequestException;
import com.example.oauth2proxy.jpa.entity.User;
import com.example.oauth2proxy.service.UserService;
import com.example.oauth2proxy.swagger.model.SignUpRequest;
import com.example.oauth2proxy.swagger.model.SuccessOperationResponse;
import jakarta.annotation.Generated;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
@Controller
@RequiredArgsConstructor
@RequestMapping("${openapi.oauth2-proxy.base-path:/oauth2-proxy/v1}")
public class SignupApiController implements SignupApi {

    private final NativeWebRequest request;

    private final UserService userService;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<SuccessOperationResponse> registerUser(SignUpRequest signUpRequest) {
        if (userService.findByEmail(signUpRequest.getEmail()).isPresent()) {
            throw new BadRequestException("Email address already in use.");
        }

        // Creating user's account
        User newUser = userService.createUser(
                User.builder()
                        .name(signUpRequest.getName())
                        .email(signUpRequest.getEmail())
                        .password(signUpRequest.getPassword())
                        .build()
        );

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me")
                .buildAndExpand(newUser.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new SuccessOperationResponse().result("User registered successfully."));
    }
}
