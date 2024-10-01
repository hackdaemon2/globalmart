package com.globalmart.app.repository;

import com.globalmart.app.entity.OrderEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends SpecificationRepository<OrderEntity, Long> {

    Optional<OrderEntity> findByGuid(String guid);

    boolean existsByOrderReference(String orderReference);

}
