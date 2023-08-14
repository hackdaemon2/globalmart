package com.example.demo.repository;

import com.example.demo.models.entities.AccessTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.Optional;

public interface AccessTokenRepository extends JpaRepository<AccessTokenEntity, BigInteger> {

    Optional<AccessTokenEntity> findByRefreshToken(String refreshToken);
}
