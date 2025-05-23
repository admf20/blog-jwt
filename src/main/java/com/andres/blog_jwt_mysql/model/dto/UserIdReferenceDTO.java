package com.andres.blog_jwt_mysql.model.dto;

import jakarta.validation.constraints.NotBlank;

public class UserIdReferenceDTO {

    @NotBlank(message = "el ID del author es obligatorio")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
