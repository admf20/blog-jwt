package com.andres.blog_jwt_mysql.repository;

import com.andres.blog_jwt_mysql.model.ERole;
import com.andres.blog_jwt_mysql.model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(ERole name);
}
