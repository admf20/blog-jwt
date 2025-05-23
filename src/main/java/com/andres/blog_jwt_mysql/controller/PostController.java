package com.andres.blog_jwt_mysql.controller;

import com.andres.blog_jwt_mysql.model.dto.PostDTO;
import com.andres.blog_jwt_mysql.response.ApiResponse;
import com.andres.blog_jwt_mysql.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    /*
    * NOTA IMPORTANTE:
    * - En el controlador por lo general sí estamos trabajando con DTO siempre utilizarlo y evitar exponer la entidad directamente
    * - Una buena practíca es devolver un ResponseEntity para tener más control de la respuesta o el mismo DTO
    * - Evitar los Optional ya, que es lógica donde corresponde al Servicio
    *
    * "En este caso estamos respondiendo con un ResponseEntity peró con una respuesta más estructurada
    *  que consta de Objeto "ApiResponse" que consta de "Estado" - "Mensaje" - "Data""
    *
    * */

    private final PostService postServices;

    public PostController(PostService postServices){
        this.postServices = postServices;
    };

    //listar todos los POSTS
    @GetMapping
    public ResponseEntity<ApiResponse<List<PostDTO>>> getAllPosts() {
        List<PostDTO> posts = postServices.getAllPost();

        ApiResponse<List<PostDTO>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Posts obtenidos correctamente",
                posts
        );
        return ResponseEntity.ok(response);
    };

    //lista POST por ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostDTO>> getAllByIdPost(@PathVariable Long id){
        PostDTO postDTO = postServices.getPostById(id);

        ApiResponse<PostDTO> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Post encontrado",
                postDTO
        );
        return ResponseEntity.ok(response);
    };

    //crear un nuevo POST
    @PostMapping
    public ResponseEntity<ApiResponse<PostDTO>> createdPost(@Valid @RequestBody PostDTO postDTO){
        PostDTO newPostDTO = postServices.createdPost(postDTO);

        ApiResponse<PostDTO> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Post creado Correctamente",
                newPostDTO
        );
        return ResponseEntity.ok(response);
    };

    //actualizar POST por ID
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PostDTO>> updatedPost(@Valid @PathVariable Long id, @RequestBody PostDTO postDetails){
        PostDTO updatedPost = postServices.updatedPost(id, postDetails);

        ApiResponse<PostDTO> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Post Actualizado correctamente",
                updatedPost
        );
        return ResponseEntity.ok(response);
    };

    //eliminar POST por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletedPost(@PathVariable Long id){
         postServices.deletedPost(id);

         ApiResponse<Void> response = new ApiResponse<>(
                 HttpStatus.NO_CONTENT.value(),
                 "Post Eliminado Correctamente",
                 null
         );
         return ResponseEntity.ok(response);
    };
}