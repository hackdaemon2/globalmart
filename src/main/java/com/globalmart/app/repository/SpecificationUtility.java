package com.globalmart.app.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public final class SpecificationUtility {

    private SpecificationUtility() {
        throw new IllegalStateException(SpecificationUtility.class.getName());
    }

    public static Predicate addPredicateIfNotEmpty(Predicate predicate, CriteriaBuilder criteriaBuilder,
                                                   Path<String> path, String value) {
        if (value != null && !value.isEmpty()) {
            return criteriaBuilder.and(predicate, criteriaBuilder.like(path, "%" + value + "%"));
        }
        return predicate;
    }

    public static <E> Predicate addDateFilter(LocalDateTime startDate, LocalDateTime stopDate,
                                              CriteriaBuilder criteriaBuilder, Predicate predicate, Root<E> root) {
        if (startDate.isAfter(stopDate)) {
            throw new IllegalArgumentException("Start date must not be after stop date.");
        }
        Date start = Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant());
        Date stop = Date.from(stopDate.atZone(ZoneId.systemDefault()).toInstant());
        predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("dateCreated"), start));
        predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("dateCreated"), stop));
        return predicate;
    }

}
