package com.andres.blog_jwt_mysql.security.filters;

import com.andres.blog_jwt_mysql.security.jwt.JwtUtils;
import com.andres.blog_jwt_mysql.service.UserDetailServices.UserDetailsServicesImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsServicesImpl userDetailsServices;

    public JwtAuthorizationFilter(JwtUtils jwtUtils, UserDetailsServicesImpl userDetailsServices){
        this.jwtUtils = jwtUtils;
        this.userDetailsServices = userDetailsServices;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        System.out.println(">>> JwtAuthorizationFilter ejecutándose para: " + request.getRequestURI());
        //capturamos el token del Header
        String tokenHeader = request.getHeader("Authorization");

        //validamos si existe el token y también si los primeros valores incluye el Bearer
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")){
            System.out.println("token actual: " + tokenHeader);
            String token = tokenHeader.substring(7); //Quitamos el Bearer del token

            if (jwtUtils.isTokenValid(token)){ //validamos el token
                /*
                * - Obtenemos el usuario, sacándolo de los Claims que tiene el token
                * - Utilizamos el metodo loadUserByUsername de UserDetailsServices donde
                *   me trae los datos del usuario pero en un objeto UserDetails
                * */
                String username = jwtUtils.getUsernameFromToken(token);
                UserDetails userDetails = userDetailsServices.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                userDetails.getAuthorities()
                        );
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
