package com.andres.blog_jwt_mysql.security;

import com.andres.blog_jwt_mysql.security.filters.JwtAuthenticationFilter;
import com.andres.blog_jwt_mysql.security.filters.JwtAuthorizationFilter;
import com.andres.blog_jwt_mysql.security.jwt.JwtUtils;
import com.andres.blog_jwt_mysql.service.UserDetailServices.UserDetailsServicesImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration //para que me escanea los Bean que tiene la clase
public class SecurityConfig {

    private final UserDetailsServicesImpl userDetailsServices;
    private final JwtUtils jwtUtils;

    public SecurityConfig(UserDetailsServicesImpl userDetailsServices, JwtUtils jwtUtils){
        this.userDetailsServices = userDetailsServices;
        this.jwtUtils = jwtUtils;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
                                                   AuthenticationManager authenticationManager) throws Exception {

        //Es muy importante crear el objeto del filtro y pasarle los parámetros que requiere
        JwtAuthenticationFilter jwtAuthenticationFilter =
                new JwtAuthenticationFilter(jwtUtils, authenticationManager);

        JwtAuthorizationFilter jwtAuthorizationFilter =
                new JwtAuthorizationFilter(jwtUtils, userDetailsServices);

        /*
        * - Es importante entender el pórque se puso "UsernamePasswordAuthenticationFilter.class"
        *   en el filtro JwtAuthorizationFilter y es para indicarle a Spring que primero valide
        *   con ese filtro y ver si tiene el token para que pueda seguir novedad y no lo ponga a
        *   iniciar sección
        * */

        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/auth/login").permitAll();
                    auth.anyRequest().authenticated();
                })
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .addFilter(jwtAuthenticationFilter)
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationManager authenticationManager (HttpSecurity httpSecurity) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(userDetailsServices)
                .passwordEncoder(passwordEncoder());

        return authenticationManagerBuilder.build();
    }
}
