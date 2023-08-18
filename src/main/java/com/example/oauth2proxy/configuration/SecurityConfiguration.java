package com.example.oauth2proxy.configuration;

import com.example.oauth2proxy.security.JwtAuthenticationFilter;
import com.example.oauth2proxy.security.RestAuthenticationEntryPoint;
import com.example.oauth2proxy.security.oauth2.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Optional;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Value("${security.headers.content-security-policy.policy-directives}")
    String contentSecurityPolicyDirectives;

    @Value("${security.csrf.protection-matcher.ant-path-request-matcher-pattern:}")
    String csrfProtectionAntPathRequestMatcherPattern;

    @Value("${security.headers.http-strict-transport-security.include-sub-domains:false}")
    boolean hstsIncludeSubDomains;

    @Value("${security.headers.http-strict-transport-security.max-age-in-seconds:31536000}")
    long hstsMaxAgeInSeconds;

    private CorsConfigurationSource corsConfigurationSource(CorsConfiguration corsConfiguration) {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http, CorsConfiguration corsConfiguration,
            HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository,
            AuthorizeHttpRequestMatcherConfigurator authorizeHttpRequestMatcherConfigurator,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            UserRegistrationOAuth2UserService userRegistrationOAuth2UserService,
            UserRegistrationOidcUserService userRegistrationOidcUserService,
            OAuth2Configurator oAuth2Configurator,
            OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler,
            OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler) throws Exception {

        return http
                .csrf(csrfConfigurer ->
                        csrfConfigurer.requireCsrfProtectionMatcher(new AntPathRequestMatcher(csrfProtectionAntPathRequestMatcherPattern)).configure(http)
                )
                // Enable CORS (Cross Origin Resource Sharing)
                .cors(corsConfigurer ->
                        corsConfigurer.configurationSource(corsConfigurationSource(corsConfiguration))
                )
                // No Session created or used by Spring Security
                .sessionManagement(sessionManagementConfigurer ->
                        sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // Exception Handling
                .exceptionHandling(exceptionHandlingConfigurer ->
                        exceptionHandlingConfigurer.authenticationEntryPoint(new RestAuthenticationEntryPoint())
                )
                // Authorized HTTP Requests
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry
                                .requestMatchers(
                                        Optional.ofNullable(authorizeHttpRequestMatcherConfigurator.getPermitAll()).orElse(new ArrayList<>()).toArray(String[]::new)
                                )
                                .permitAll()
                                .requestMatchers(
                                        Optional.ofNullable(authorizeHttpRequestMatcherConfigurator.getDenyAll()).orElse(new ArrayList<>()).toArray(String[]::new)
                                )
                                .denyAll()
                                .anyRequest()
                                .authenticated()
                )
                // HTTP Header Configuration
                // 1. Cache Control (Default: Enabled)
                // 2. Content Type Option (Default: Enabled)
                // 3. XSS Protection (Default: Enabled)
                // 4. Content-Security-Policy (Configured in application.yml)
                // 5. HTTP Strict Transport Policy (Configured in application.yml)
                // 6. Frame Options (Deny)
                .headers(headersConfigurer ->
                        headersConfigurer
                                .contentSecurityPolicy(
                                        contentSecurityPolicyConfig -> contentSecurityPolicyConfig.policyDirectives(contentSecurityPolicyDirectives)
                                )
                                .httpStrictTransportSecurity(hstsConfig ->
                                        hstsConfig
                                                .includeSubDomains(hstsIncludeSubDomains)
                                                .maxAgeInSeconds(hstsMaxAgeInSeconds)
                                )
                                .frameOptions(HeadersConfigurer.FrameOptionsConfig::deny))
                // OAuth2 Login Configuration
                .oauth2Login(httpSecurityOAuth2LoginConfigurer ->
                        httpSecurityOAuth2LoginConfigurer
                                .authorizationEndpoint(authorizationEndpointConfig ->
                                        authorizationEndpointConfig
                                                .baseUri(oAuth2Configurator.getAuthorizationEndpointBaseUri())
                                                .authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository)
                                )
                                .redirectionEndpoint(redirectionEndpointConfig ->
                                        redirectionEndpointConfig.baseUri(oAuth2Configurator.getRedirectionEndpointBaseUri())
                                )
                                .userInfoEndpoint(userInfoEndpointConfig ->
                                        userInfoEndpointConfig
                                                .userService(userRegistrationOAuth2UserService)
                                                .oidcUserService(userRegistrationOidcUserService)
                                )
                                .successHandler(oAuth2AuthenticationSuccessHandler)
                                .failureHandler(oAuth2AuthenticationFailureHandler)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}