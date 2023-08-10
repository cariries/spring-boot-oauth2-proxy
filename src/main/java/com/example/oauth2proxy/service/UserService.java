package com.example.oauth2proxy.service;

import com.example.oauth2proxy.jpa.entity.User;
import com.example.oauth2proxy.jpa.repository.UserRepository;
import com.example.oauth2proxy.model.AuthorizationProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@Transactional
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Transactional
    public User createUser(User user) {
        User newUser = new User();
        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setProvider(AuthorizationProvider.INTERNAL);
        newUser.setProviderId(UUID.randomUUID().toString());
        return userRepository.save(newUser);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
