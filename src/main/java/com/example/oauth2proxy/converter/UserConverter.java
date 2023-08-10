package com.example.oauth2proxy.converter;

import com.example.oauth2proxy.swagger.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter implements BaseConverter<User, com.example.oauth2proxy.jpa.entity.User>{

    @Override
    public com.example.oauth2proxy.jpa.entity.User transform(User entity) {
        com.example.oauth2proxy.jpa.entity.User result = null;
        if (entity != null) {
            result = new com.example.oauth2proxy.jpa.entity.User();

            result.setName(entity.getName());
            result.setEmail(entity.getEmail());
            result.setImageUrl(entity.getAvatar());
        }
        return result;
    }

    @Override
    public User reverseTransform(com.example.oauth2proxy.jpa.entity.User entity) {
        User result = null;
        if (entity != null) {
            result = new User();

            result.setName(entity.getName());
            result.setEmail(entity.getEmail());
            result.setAvatar(entity.getImageUrl());
        }
        return result;
    }
}
