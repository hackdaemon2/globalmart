package com.globalmart.app.repository;


import com.globalmart.app.entity.RoleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Long> {

    Set<RoleEntity> findByGuidInAndDeletedIsFalse(List<String> id);

}
