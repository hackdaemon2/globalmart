package com.globalmart.app.repository;

import com.globalmart.app.entity.InventoryEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends SpecificationRepository<InventoryEntity, Long> {
}
