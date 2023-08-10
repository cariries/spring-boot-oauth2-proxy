package com.example.oauth2proxy.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BasicAuditTrailEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -7341958630015548169L;

    @CreatedBy
    @Column(name = "create_user")
    protected String createUser;

    @CreatedDate
    @Column(name = "create_timestamp")
    protected OffsetDateTime createTimestamp;

    @LastModifiedBy
    @Column(name = "last_update_user")
    protected String lastUpdateUser;

    @LastModifiedDate
    @Column(name = "last_update_timestamp")
    protected OffsetDateTime lastUpdateTimestamp;
}