package com.andres.blog_jwt_mysql.security.filters;

import com.andres.blog_jwt_mysql.model.UserEntity;
import com.andres.blog_jwt_mysql.security.jwt.JwtUtils;
import com.andres.blog_jwt_mysql.util.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    /*Porque extendemos el UsernamePasswordAuthenticationFilter
    - "UsernamePasswordAuthenticationFilter" es el filtro de Spring Security encargado de interceptar
       peticiones de login, extraer las credenciales y delegar la autenticación al AuthenticationManager
    */

    private final JwtUtils jwtUtils;

    public JwtAuthenticationFilter(JwtUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.jwtUtils = jwtUtils;

        /*Aclaración de porque estamos setando el AuthenticationManager
        * - Es necesario hacerlo de manera manual ya que el filtro no sabe del manager y es
        *   necesario hacerlo explicito
        */
        setAuthenticationManager(authenticationManager);
        setFilterProcessesUrl("/auth/login");
    }

    //para la autenticación
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            //convertimos lo que me retorna el cliente que es un JSON a un objeto UserEntity gracias al ObjectMapper de Jakarta
            UserEntity user = new ObjectMapper().readValue(
                    request.getInputStream(), // lo que devuelve el cliente
                    UserEntity.class //que se convierta a una entidad
            );

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            user.getPassword()
                    );

            return getAuthenticationManager().authenticate(authToken);
            /*Proceso Interno que pasa cuando el AuthenticationManager
            * - Llama a tu implementación de UserDetailsService (UserDetailsServicesImpl)
              - Busca al usuario por username
              - Compara la contraseña usando el PasswordEncoder
            ✅ Si todo es correcto devuelve un objeto Authentication valido
            ❌ Si falla, lanza una excepción de autenticación.*/
        }catch (IOException e) {
            throw new RuntimeException("Credenciales Incorrectas");
        }
    }

    //cuando se autentica correctamente lo que le vamos a devolver al cliente
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException{

        //obtengo la información del usuario con getPrincipal y hago un casteo explicito de UserDetails ya, que me retorna un Objeto
        UserDetails user = (UserDetails) authResult.getPrincipal();//me retorna el un UserDetails
        String token = jwtUtils.generateAccesToken(user.getUsername());//generamos el token

        Map<String, Object> httpResponseBody = new HashMap<>();
        httpResponseBody.put("status: ", HttpStatus.OK.value());
        httpResponseBody.put("messages: ", "Usuario ".concat(user.getUsername()).concat(" Autenticado Correctamente!!"));
        httpResponseBody.put("token: ", token);
        httpResponseBody.put("usuario: ", user.getUsername());

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(new ObjectMapper().writeValueAsString(httpResponseBody));
        response.getWriter().flush();
    }

    /*private void buildErrorResponse(HttpServletResponse response, int status, String message) {
        ApiResponse<Object> error = new ApiResponse<>(status, message, null);

        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String json = new ObjectMapper().writeValueAsString(error);
            response.getWriter().write(json);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }*/
}
