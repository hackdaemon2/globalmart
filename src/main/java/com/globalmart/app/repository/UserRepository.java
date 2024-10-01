package com.globalmart.app.repository;

import com.globalmart.app.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends SpecificationRepository<UserEntity, Long> {

    @Query(value = "SELECT u FROM UserEntity u WHERE u.deleted = false AND u.username = ?1")
    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String username);

}
