package com.andres.blog_jwt_mysql.repository;

import com.andres.blog_jwt_mysql.model.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
}
