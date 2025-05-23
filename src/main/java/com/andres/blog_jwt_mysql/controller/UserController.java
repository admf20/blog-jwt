package com.andres.blog_jwt_mysql.controller;

import com.andres.blog_jwt_mysql.model.dto.UserDTO;
import com.andres.blog_jwt_mysql.response.ApiResponse;
import com.andres.blog_jwt_mysql.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        List<UserDTO> users = userService.getAllUser();

        ApiResponse<List<UserDTO>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Usuarios obtenidos correctamente",
                users
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getByIdUser(@PathVariable Long id){
        UserDTO user = userService.getUserById(id);

        ApiResponse<UserDTO> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Usuarios obtenido correctamente",
                user
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserDTO>> createdUser(@RequestBody UserDTO userDTO){
        UserDTO newUser = userService.createdUser(userDTO);

        ApiResponse<UserDTO> response = new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Usuario creado correctamente",
                newUser
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> udpatedUser(@Valid @PathVariable Long id, @RequestBody UserDTO userDetails){
        UserDTO uptedUser = userService.updatedUser(id, userDetails);

        ApiResponse<UserDTO> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Usuario Actualizado correctamente",
                uptedUser
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletedUser(@PathVariable Long id){
        userService.deletedUser(id);

        ApiResponse<Void> response = new ApiResponse<>(
                HttpStatus.NO_CONTENT.value(),
                "Usuario Eliminado correctamente",
                null
        );
        return ResponseEntity.ok(response);
    }
}
