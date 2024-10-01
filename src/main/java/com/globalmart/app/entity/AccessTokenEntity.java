package com.globalmart.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(
        name = "access_tokens",
        indexes = {
                @Index(name = "idx_refresh_token", columnList = "refresh_token")
        })
@NoArgsConstructor
public class AccessTokenEntity extends AbstractEntity {

    @Column(name = "token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "issue_date")
    private Timestamp issuedAt;

    @Column(name = "expiration_date")
    private Timestamp expiration;

    @Column(name = "refresh_token_expiration_date")
    private Timestamp refreshTokenExpiration;

}
