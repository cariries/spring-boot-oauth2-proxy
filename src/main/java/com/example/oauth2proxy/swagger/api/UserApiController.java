package com.example.oauth2proxy.swagger.api;

import com.example.oauth2proxy.converter.UserConverter;
import com.example.oauth2proxy.exception.ResourceNotFoundException;
import com.example.oauth2proxy.service.UserService;
import com.example.oauth2proxy.swagger.model.User;
import jakarta.annotation.Generated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
@Controller
@RequiredArgsConstructor
@RequestMapping("${openapi.oauth2-proxy.base-path:/oauth2-proxy/v1}")
@Slf4j
public class UserApiController implements UserApi {

    private final NativeWebRequest request;

    private final UserService userService;

    private final UserConverter userConverter;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Override
    public ResponseEntity<User> getCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.findByEmail(userDetails.getUsername())
                .map(userConverter::reverseTransform)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("User", "name", userDetails.getUsername()));
    }
}
