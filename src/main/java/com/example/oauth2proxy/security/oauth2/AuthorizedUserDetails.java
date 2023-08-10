package com.example.oauth2proxy.security.oauth2;

import com.example.oauth2proxy.jpa.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@Getter
@Builder
@AllArgsConstructor
public class AuthorizedUserDetails implements OAuth2User, UserDetails {

    private static final long serialVersionUID = 7999600335892551495L;

    private UUID id;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private transient Map<String, Object> attributes;

    @Override
    public String getName() {
        return id != null ? id.toString() : null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static AuthorizedUserDetails newInstance(User user, Map<String, Object> attributes) {
        // TODO: Setup User Role (Depends on role mapping of Current User)
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        return AuthorizedUserDetails.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .attributes(attributes)
                .authorities(authorities)
                .build();
    }
}