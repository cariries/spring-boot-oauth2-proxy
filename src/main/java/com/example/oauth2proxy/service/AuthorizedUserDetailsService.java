package com.example.oauth2proxy.service;

import com.example.oauth2proxy.constant.JwtExtendedClaimNames;
import com.example.oauth2proxy.security.oauth2.AuthorizedUserDetails;
import com.example.oauth2proxy.jpa.entity.User;
import com.example.oauth2proxy.jpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class AuthorizedUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email : " + username)
                );

        // Setup User Attributes
        Map<String, Object> attributes = new HashMap<>();
        attributes.putAll(
                Map.of(
                        JwtExtendedClaimNames.OID, user.getId(),
                        JwtExtendedClaimNames.NAME, user.getName(),
                        JwtExtendedClaimNames.EMAIL, user.getEmail()
                )
        );
        return AuthorizedUserDetails.newInstance(user, attributes);
    }
}