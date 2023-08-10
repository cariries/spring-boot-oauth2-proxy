package com.example.oauth2proxy.swagger.api;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.NativeWebRequest;

import java.io.IOException;

public final class ApiUtil {

    private ApiUtil() {

    }

    public static void setExampleResponse(NativeWebRequest req, String contentType, String example) {
        try {
            HttpServletResponse res = req.getNativeResponse(HttpServletResponse.class);
            if (res != null) {
                res.setCharacterEncoding("UTF-8");
                res.addHeader("Content-Type", contentType);
                res.getWriter().print(example);
            }
        } catch (IOException e) {
            throw new SwaggerApiResourceException(e);
        }
    }

    static class SwaggerApiResourceException extends RuntimeException {

        public SwaggerApiResourceException(String message) {
            super(message);
        }

        public SwaggerApiResourceException(String message, Throwable cause) {
            super(message, cause);
        }

        public SwaggerApiResourceException(Throwable cause) {
            super(cause);
        }
    }
}
