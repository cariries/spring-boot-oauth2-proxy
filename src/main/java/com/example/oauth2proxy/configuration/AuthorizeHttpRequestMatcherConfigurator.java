package com.example.oauth2proxy.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.AntPathMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizeHttpRequestMatcherConfigurator {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private List<String> permitAll;
    private List<String> denyAll;

    public boolean isUrlsAllowed(String requestUri) {
        boolean denyUrl = Optional.ofNullable(denyAll).orElse(new ArrayList<>()).stream().anyMatch(s -> antPathMatcher.match(s, requestUri));
        boolean permitUrl = Optional.ofNullable(permitAll).orElse(new ArrayList<>()).stream().anyMatch(s -> antPathMatcher.match(s, requestUri));
        return (permitUrl && !denyUrl);
    }
}