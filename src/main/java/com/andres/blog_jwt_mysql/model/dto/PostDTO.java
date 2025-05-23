package com.andres.blog_jwt_mysql.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class PostDTO {

    private Long id;

    @NotBlank(message = "El campo es necesario")
    @Size(max = 20)
    private String title;

    @NotBlank(message = "El campo es necesario")
    @Size(max = 50)
    private String content;

    private LocalDateTime createdAt;

    private UserIdReferenceDTO author;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UserIdReferenceDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserIdReferenceDTO author) {
        this.author = author;
    }
}
