package com.globalmart.app.repository;

import com.globalmart.app.dto.UserFilterDTO;
import com.globalmart.app.entity.UserEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import static com.globalmart.app.repository.SpecificationUtility.addDateFilter;
import static com.globalmart.app.repository.SpecificationUtility.addPredicateIfNotEmpty;

public class UserSpecification {

    private UserSpecification() {
        throw new IllegalStateException(UserSpecification.class.getName());
    }

    public static Specification<UserEntity> withFilters(UserFilterDTO filter) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            predicate = addPredicateIfNotEmpty(predicate, criteriaBuilder, root.get("firstName"), filter.firstName());
            predicate = addPredicateIfNotEmpty(predicate, criteriaBuilder, root.get("lastName"), filter.lastName());
            predicate = addPredicateIfNotEmpty(predicate, criteriaBuilder, root.get("username"), filter.username());
            predicate = addPredicateIfNotEmpty(predicate, criteriaBuilder, root.get("email"), filter.email());
            predicate = addPredicateIfNotEmpty(predicate, criteriaBuilder, root.get("phone"), filter.phone());
            predicate = addDateFilter(filter.startDate(), filter.stopDate(), criteriaBuilder, predicate, root);

            return predicate;
        };
    }

}
