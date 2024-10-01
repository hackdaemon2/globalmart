package com.globalmart.app.repository;

import com.globalmart.app.dto.OrderFilterDTO;
import com.globalmart.app.entity.OrderEntity;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import static com.globalmart.app.repository.SpecificationUtility.addDateFilter;
import static com.globalmart.app.repository.SpecificationUtility.addPredicateIfNotEmpty;

public final class OrderSpecification {

    private OrderSpecification() {
        throw new IllegalStateException(UserSpecification.class.getName());
    }

    public static Specification<OrderEntity> withFilters(OrderFilterDTO filter) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            predicate = addPredicateIfNotEmpty(predicate, criteriaBuilder, root.join("user", JoinType.INNER).get("guid"), filter.userId());
            predicate = addPredicateIfNotEmpty(predicate, criteriaBuilder, root.get("guid"), filter.id());
            predicate = addPredicateIfNotEmpty(predicate, criteriaBuilder, root.get("currency"), filter.currency());
            predicate = addPredicateIfNotEmpty(predicate, criteriaBuilder, root.get("status"), filter.status());
            predicate = addDateFilter(filter.startDate(), filter.stopDate(), criteriaBuilder, predicate, root);

            return predicate;
        };
    }
}

