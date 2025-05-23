package com.andres.blog_jwt_mysql.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.Set;

public class UserDTO {

    private Long id;

    @NotBlank(message = "El campo es necesario")
    @UniqueElements
    @Size(max = 30)
    private String username;

    @NotBlank(message = "El campo es necesario")
    @Email(message = "Valide nuevamente el correo, por favor ")
    private String email;

    @NotBlank(message = "El campo es necesario")
    private String password;

    @NotNull(message = "El campo es obligatorio")
    private Set<String> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
