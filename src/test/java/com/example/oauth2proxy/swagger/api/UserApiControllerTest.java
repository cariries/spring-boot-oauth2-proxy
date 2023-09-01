package com.example.oauth2proxy.swagger.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.context.request.NativeWebRequest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class UserApiControllerTest {

    @InjectMocks
    UserApiController userApiController;

    @Mock
    NativeWebRequest request;

    @Test
    void getRequest() {
        assertTrue(userApiController.getRequest().isPresent());
    }

    @Test
    void getCurrentUser() {
    }
}