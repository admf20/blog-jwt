package com.andres.blog_jwt_mysql.mapper;

import com.andres.blog_jwt_mysql.model.PostEntity;
import com.andres.blog_jwt_mysql.model.UserEntity;
import com.andres.blog_jwt_mysql.model.dto.PostDTO;
import com.andres.blog_jwt_mysql.model.dto.UserIdReferenceDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PostMapper {

    //recibo un post y lo convierto a DTOPost
    public PostDTO toPostDTO(PostEntity post){
        PostDTO dto_post = new PostDTO();

        dto_post.setTitle(post.getTitle());
        dto_post.setContent(post.getContent());
        dto_post.setCreatedAt(post.getCreatedAt());
        //dto_post.setAuthor(toUserDTO(post.getAuthor()));
        dto_post.setAuthor(toUserIdReferenceDTO(post.getAuthor()));//se almacena solo el ID del usuario
        return dto_post;
    }

    //recibo un user y le retorno el id del usuario
    public UserIdReferenceDTO toUserIdReferenceDTO(UserEntity user){
        UserIdReferenceDTO userReference = new UserIdReferenceDTO();
        userReference.setId(user.getId());
        return userReference;
    }

    //recibo un PostDTO, author por la entidad User y lo convierto a entidad Post
    public PostEntity toEntity(PostDTO postDTO, UserEntity author){
        PostEntity post = new PostEntity();
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setCreatedAt(LocalDateTime.now());
        post.setAuthor(author);
        return post;
    }


}
