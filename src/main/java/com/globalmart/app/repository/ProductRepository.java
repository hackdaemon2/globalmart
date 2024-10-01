package com.globalmart.app.repository;

import com.globalmart.app.entity.ProductEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends SpecificationRepository<ProductEntity, Long> {

    Optional<ProductEntity> findByProductCode(String productCode);

    Optional<ProductEntity> findByGuid(String guid);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END FROM ProductEntity p WHERE p.guid IN :guids")
    List<Boolean> existsByGuidIn(List<String> guidList);

}
