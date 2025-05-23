package com.andres.blog_jwt_mysql.mapper;

import com.andres.blog_jwt_mysql.model.ERole;
import com.andres.blog_jwt_mysql.model.RoleEntity;
import com.andres.blog_jwt_mysql.model.UserEntity;
import com.andres.blog_jwt_mysql.model.dto.UserDTO;
import com.andres.blog_jwt_mysql.repository.RoleRepository;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final RoleRepository roleRepository;

    public UserMapper(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    //user a userDTO
    public UserDTO toUserDTO(UserEntity user){

        UserDTO dto_user = new UserDTO();
        dto_user.setId(user.getId());
        dto_user.setUsername(user.getUsername());
        dto_user.setEmail(user.getEmail());
        dto_user.setPassword(user.getPassword());
        Set<String> roles = user.getRoles().stream()
                .map(roleEntity -> roleEntity.getName().name())
                .collect(Collectors.toSet());

        dto_user.setRoles(roles);
        return dto_user;
    }

    //userDTO a user
    public UserEntity toUserEntity(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userDTO.getUsername());
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setPassword(userDTO.getPassword());

        System.out.println(userDTO.getRoles());

        Set<RoleEntity> roles = userDTO.getRoles().stream()
                .map(roleStr -> {
                    try {
                        // Validar que el rol existe en el enum
                        ERole roleEnum = ERole.valueOf(roleStr);

                        // Buscar en la base de datos la entidad RoleEntity correspondiente
                        return roleRepository.findByName(roleEnum)
                                .orElseThrow(() -> new RuntimeException("El rol no existe en la base de datos: " + roleEnum));

                    } catch (IllegalArgumentException ex) {
                        // Si el string no es un valor válido del enum ERole - línea 51
                        throw new RuntimeException("Rol inválido ingresado: " + roleStr);
                    }
                })
                .collect(Collectors.toSet());

        userEntity.setRoles(roles);
        return userEntity;
    }

}
