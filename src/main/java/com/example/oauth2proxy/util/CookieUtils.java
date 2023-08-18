package com.example.oauth2proxy.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.owasp.encoder.Encode;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

public final class CookieUtils {

    private CookieUtils() {

    }

    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        return Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(name)).findFirst();
    }

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, Encode.forJava(value));
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(name)).forEach(cookie -> addCookie(response, name, StringUtils.EMPTY, 0));
    }

    public static String serialize(Serializable object) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(object));
    }

    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(SerializationUtils.deserialize(
                Base64.getUrlDecoder().decode(cookie.getValue())));
    }
}