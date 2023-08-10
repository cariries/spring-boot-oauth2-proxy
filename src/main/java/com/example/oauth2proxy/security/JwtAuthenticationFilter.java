package com.example.oauth2proxy.security;

import com.example.oauth2proxy.security.oauth2.TokenProvider;
import com.example.oauth2proxy.configuration.AuthorizeHttpRequestMatcherConfigurator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_HEADER = "Bearer ";

    @Autowired
    AuthorizeHttpRequestMatcherConfigurator authorizeHttpRequestMatcherConfigurator;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final boolean permitAllUrlsMatched = authorizeHttpRequestMatcherConfigurator.isUrlsAllowed(request.getRequestURI());
        final boolean permitMethodsMatched = Arrays.asList(RequestMethod.OPTIONS.name()).contains(request.getMethod());

        if (!permitAllUrlsMatched && !permitMethodsMatched) {
            final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            final boolean bearerTokenHeaderFound = (authorizationHeader != null && authorizationHeader.startsWith(BEARER_HEADER));

            if (bearerTokenHeaderFound) {
                final String jwtToken = StringUtils.substring(authorizationHeader, BEARER_HEADER.length());

                if (StringUtils.isNotBlank(jwtToken) && tokenProvider.validateToken(jwtToken)) {
                    String subjectFromToken = tokenProvider.getSubjectFromToken(jwtToken);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(subjectFromToken);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}