package com.example.oauth2proxy.configuration;

import com.example.oauth2proxy.security.oauth2.OAuth2Configurator;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

@Configuration
public class ApplicationConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "security.cors")
    CorsConfiguration corsConfiguration() {
        return new CorsConfiguration();
    }

    @Bean
    @ConfigurationProperties(prefix = "security.jwk")
    JwkConfigurator jwkConfigurator() {
        return new JwkConfigurator();
    }

    @Bean
    @ConfigurationProperties(prefix = "security.oauth2")
    OAuth2Configurator oAuth2Configurator() {
        return new OAuth2Configurator();
    }

    @Bean
    @ConfigurationProperties(prefix = "security.authorized-request-matcher")
    AuthorizeHttpRequestMatcherConfigurator authorizeHttpRequestMatcherConfigurator() {
        return new AuthorizeHttpRequestMatcherConfigurator();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        final int strength = 4;
        final SecureRandom secureRandom = new SecureRandom(RandomStringUtils.randomAlphabetic(32).getBytes(StandardCharsets.UTF_8));
        return new BCryptPasswordEncoder(strength, secureRandom);
    }
}
