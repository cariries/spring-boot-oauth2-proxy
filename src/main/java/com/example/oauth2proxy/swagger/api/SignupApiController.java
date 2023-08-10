package com.example.oauth2proxy.swagger.api;

import com.example.oauth2proxy.jpa.entity.User;
import com.example.oauth2proxy.service.UserService;
import com.example.oauth2proxy.swagger.model.SignUpRequest;
import com.example.oauth2proxy.swagger.model.SuccessOperationResponse;
import com.example.oauth2proxy.exception.BadRequestException;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
@Controller
@RequestMapping("${openapi.oauth2-proxy.base-path:/oauth2-proxy/v1}")
public class SignupApiController implements SignupApi {

    private final NativeWebRequest request;

    @Autowired
    public SignupApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Autowired
    UserService userService;

    @Override
    public ResponseEntity<SuccessOperationResponse> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
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
