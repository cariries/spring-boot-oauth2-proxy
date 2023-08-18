package com.example.oauth2proxy.security.oauth2.user;

import com.example.oauth2proxy.exception.OAuth2AuthenticationProcessingException;
import com.example.oauth2proxy.model.AuthorizationProvider;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public final class AuthorizedUserInfoFactory {

    private AuthorizedUserInfoFactory() {

    }

    private static Map<String, Class<? extends AuthorizedUserInfo>> implInvokeMap =
            Collections.synchronizedMap(new HashMap<>());

    public static AuthorizedUserInfo newInstance(final String registrationId, final Map<String, Object> attributes) throws OAuth2AuthenticationProcessingException {
        AuthorizationProvider authorizationProvider = AuthorizationProvider.fromEnumeration(registrationId);
        String implementedClassName = String.format("%1$s.%2$s",
                AuthorizedUserInfo.class.getPackageName(), authorizationProvider.getImplementationClassName());

        try {
            implInvokeMap.putIfAbsent(implementedClassName, (Class<? extends AuthorizedUserInfo>) Class.forName(implementedClassName));
            return implInvokeMap.get(implementedClassName).getDeclaredConstructor(Map.class).newInstance(attributes);
        } catch (ClassNotFoundException e) {
            throw new OAuth2AuthenticationProcessingException("OAuth2UserInfo Implementation Class Not Found. [Class Name: " + implementedClassName + "]", e);
        } catch (InstantiationException e) {
            throw new OAuth2AuthenticationProcessingException("OAuth2UserInfo Instantiation Exception. [Class Name: " + implementedClassName + "]", e);
        } catch (IllegalAccessException e) {
            throw new OAuth2AuthenticationProcessingException("OAuth2UserInfo Illegal Access Exception [Class Name: " + implementedClassName + "]", e);
        } catch (InvocationTargetException e) {
            throw new OAuth2AuthenticationProcessingException("OAuth2UserInfo Invocation Exception [Class Name: " + implementedClassName + "]", e);
        } catch (NoSuchMethodException e) {
            throw new OAuth2AuthenticationProcessingException("OAuth2UserInfo Default Attribute Map Constructor Not Found [Class Name: " + implementedClassName + "]", e);
        }
    }

    public static AuthorizedUserInfo newInstance(final Map<String, Object> attributes) throws OAuth2AuthenticationProcessingException {
        return new InternalUserInfo(attributes);
    }
}