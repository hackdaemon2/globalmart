package com.globalmart.app.repository;

import com.globalmart.app.dto.ProductFilterDTO;
import com.globalmart.app.entity.ProductEntity;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import static com.globalmart.app.repository.SpecificationUtility.addDateFilter;
import static com.globalmart.app.repository.SpecificationUtility.addPredicateIfNotEmpty;

public class ProductSpecification {

    private ProductSpecification() {
        throw new IllegalStateException(ProductSpecification.class.getName());
    }

    public static Specification<ProductEntity> withFilters(ProductFilterDTO filter) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            predicate = addPredicateIfNotEmpty(predicate, criteriaBuilder, root.get("name"), filter.name());
            predicate = addPredicateIfNotEmpty(predicate, criteriaBuilder, root.get("currency"), filter.currency());
            predicate = addPredicateIfNotEmpty(predicate, criteriaBuilder, root.get("productCode"), filter.productCode());
            predicate = addPredicateIfNotEmpty(predicate, criteriaBuilder, root.join("user", JoinType.INNER).get("guid"), filter.userID());
            predicate = addPredicateIfNotEmpty(predicate, criteriaBuilder, root.get("price"), filter.price().toString());
            predicate = addDateFilter(filter.startDate(), filter.stopDate(), criteriaBuilder, predicate, root);

            return predicate;
        };
    }

}