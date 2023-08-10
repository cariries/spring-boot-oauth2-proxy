package com.example.oauth2proxy.jpa.entity;

import com.example.oauth2proxy.model.AuthorizationProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
@EqualsAndHashCode(callSuper = false)
public class User extends BasicAuditTrailEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 2607587385705920440L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Email
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "image_url", length = 1024)
    private String imageUrl;

    @Builder.Default
    @Column(name = "email_verified", nullable = false)
    private Boolean emailVerified = Boolean.FALSE;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "provider", length = 50)
    private AuthorizationProvider provider;

    @Column(name = "provider_id", length = 50)
    private String providerId;

    @Builder.Default
    @Column(name = "status", nullable = false)
    private Boolean active = Boolean.TRUE;

    @JsonIgnore
    @Column(name = "password", length = 1024)
    private String password;

    @Builder.Default
    @Version
    @Column(name = "version", nullable = false)
    private Long version = 0L;
}