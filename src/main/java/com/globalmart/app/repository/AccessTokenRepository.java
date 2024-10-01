package com.globalmart.app.repository;

import com.globalmart.app.entity.AccessTokenEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccessTokenRepository extends CrudRepository<AccessTokenEntity, Long> {

    Optional<AccessTokenEntity> findByRefreshToken(String refreshToken);

}
