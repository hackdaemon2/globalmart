package com.globalmart.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpecificationRepository<T, R> extends JpaRepository<T, R> {

    Optional<T> findByGuidAndDeletedIsFalse(String guid);

    Page<T> findAll(Specification<T> specification, Pageable pageable);

    default Page<T> findAll(Pageable pageable) {
        return findAll((Specification<T>) null, pageable);
    }

}
