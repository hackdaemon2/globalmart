package com.example.demo.repository;


import com.example.demo.models.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

public interface RoleRepository extends JpaRepository<RoleEntity, BigInteger> {

    Set<RoleEntity> findByIdInAndDeletedIsFalse(List<BigInteger> id);
}
