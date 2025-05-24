package com.andres.blog_jwt_mysql.service.security;

import com.andres.blog_jwt_mysql.model.UserEntity;
import com.andres.blog_jwt_mysql.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServicesImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServicesImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario por el ".concat(username).concat("no existe en la BD")));

        System.out.println(user.getUsername());
        return new UserDetailsImpl(user);
    }
}
