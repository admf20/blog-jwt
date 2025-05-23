package com.andres.blog_jwt_mysql.service;

import com.andres.blog_jwt_mysql.mapper.UserMapper;
import com.andres.blog_jwt_mysql.model.ERole;
import com.andres.blog_jwt_mysql.model.RoleEntity;
import com.andres.blog_jwt_mysql.model.UserEntity;
import com.andres.blog_jwt_mysql.model.dto.UserDTO;
import com.andres.blog_jwt_mysql.repository.RoleRepository;
import com.andres.blog_jwt_mysql.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RestController
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       UserMapper userMapper){

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
    }

    //todos los usuarios
    public List<UserDTO> getAllUser(){
        List<UserEntity> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toUserDTO)
                .collect(Collectors.toList());
    }

    //Usuario por ID
    public UserDTO getUserById(Long id){
       UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado por el ID: ".concat(id.toString())));

       return userMapper.toUserDTO(user);
    }

    //crear usuario
    public UserDTO createdUser(UserDTO user_dto){
        //validar si ya existe usuarios por email
        System.out.println(user_dto.getEmail());
        if (userRepository.existsByEmail(user_dto.getEmail())){
            throw new RuntimeException("Ya existe en la BD el Email: ".concat(user_dto.getEmail().concat(" por favor ingresar nuevamente")));
        }
        //convertir el DTO a Entidad
        UserEntity user = userMapper.toUserEntity(user_dto);
        UserEntity newUser = userRepository.save(user);

        return userMapper.toUserDTO(newUser);
    }

    //actualizar usuario por ID
    public UserDTO updatedUser(Long id, UserDTO userDetails){
        //buscar Usuario y mapear con la nueva información
        UserEntity user_updated = userRepository.findById(id)
                .map(newUser -> {

                    System.out.println(userDetails.getEmail());
                    if (!newUser.getEmail().equals(userDetails.getEmail()) &&
                            userRepository.existsByEmail(userDetails.getEmail())) {
                        throw new RuntimeException("Ya existe un usuario con el email: " + userDetails.getEmail());
                    }

                    newUser.setUsername(userDetails.getUsername());
                    newUser.setEmail(userDetails.getEmail());
                    newUser.setPassword(userDetails.getPassword());

                    Set<RoleEntity> roles = userDetails.getRoles().stream()
                            .map(roleStr -> {
                                        try {
                                            ERole roleEnum = ERole.valueOf(roleStr);
                                            return roleRepository.findByName(roleEnum)
                                                    .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + roleEnum));
                                        } catch (IllegalArgumentException e) {
                                            throw new RuntimeException("Rol inválido: " + roleStr);
                                        }
                                    })
                            .collect(Collectors.toSet());

                    newUser.setRoles(roles);
                    return userRepository.save(newUser);
                })
                .orElseThrow(() -> new RuntimeException("El usuario que desea actualizar no existe"));
        return userMapper.toUserDTO(user_updated);
    }

    //eliminar usuario por ID
    public void deletedUser(Long id){
        UserEntity userDeleted = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El Usuario que desea elimar no existe"));

        userRepository.delete(userDeleted);
    }
}
