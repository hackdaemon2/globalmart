package com.example.demo.repository;

import com.example.demo.models.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, BigInteger> {

    @Query(value = "select u from UserEntity u where u.deleted = false and u.username = ?1")
    Optional<UserEntity> getUserDetails(String username);

    @Query(value = "select u from UserEntity u where u.deleted = false")
    Page<UserEntity> findAll(Pageable pageable);
}
