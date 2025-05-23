package com.andres.blog_jwt_mysql.service;

import com.andres.blog_jwt_mysql.mapper.PostMapper;
import com.andres.blog_jwt_mysql.model.PostEntity;
import com.andres.blog_jwt_mysql.model.UserEntity;
import com.andres.blog_jwt_mysql.model.dto.PostDTO;
import com.andres.blog_jwt_mysql.repository.PostRepository;
import com.andres.blog_jwt_mysql.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RestController
public class PostService {

    /*
    * NOTA IMPORTANTE:
    * - Siempre la mejor opción es trabajar con DTO para no exponer las entidades
    * - los Optional solo se utiliza aca en el Services y no en los controladores
    * - Cuando nosotros creamos Excepciones manuales, se debe crear un GobalExceptional que las reconozca para que se las envíe al controlador
    * */


    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;

    public PostService(PostRepository postRepository,
                       PostMapper postMapper,
                       UserRepository userRepository){

        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.userRepository = userRepository;
    }

    //todos los post
    public List<PostDTO> getAllPost(){
        List<PostEntity> posts = postRepository.findAll();
        return posts.stream()
                .map(postMapper::toPostDTO)
                .collect(Collectors.toList());
    }

    //post por id
    public PostDTO getPostById(Long id){
        PostEntity post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post no encontrado por el ID"));

        return postMapper.toPostDTO(post);
    }

    //crear post
    public PostDTO createdPost(PostDTO post_dto){
        //buscar el auhor del post
        Long author_id = post_dto.getAuthor().getId(); //capturamos el id del author
        UserEntity author = userRepository.findById(author_id)
                .orElseThrow(() -> new RuntimeException("Author no encontrado, valida nuevamente"));

        //convertir el DTO a entidad
        PostEntity post = postMapper.toEntity(post_dto, author);

        //guardar el post
        PostEntity newPost = postRepository.save(post);

        //devolver el DTOPost
        return postMapper.toPostDTO(newPost);
    }

    //actualizar post
    public PostDTO updatedPost(Long id, PostDTO postDetails){
        //buscar el Post y mapear con la nueva información
        PostEntity post_updated = postRepository.findById(id)
                .map(newPost -> {
                    newPost.setTitle(postDetails.getTitle());
                    newPost.setContent(postDetails.getContent());
                    return postRepository.save(newPost);
                })
                .orElseThrow(() -> new RuntimeException("El Post que desea actualizar no existe"));
        //retorno el dto
        return postMapper.toPostDTO(post_updated);
    }

    //eliminar post
    public void deletedPost(Long id){
        PostEntity deletedPost = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El Post de desea eliminar no existe"));

        postRepository.delete(deletedPost);
    }
}
