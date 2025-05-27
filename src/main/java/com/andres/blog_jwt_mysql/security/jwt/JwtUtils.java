package com.andres.blog_jwt_mysql.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtils {

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.time.expiration}")
    private String timeExpiration;

    //obtener la firma del token
    public Key getSignatureKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //generar el token
    public String generateAccesToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(timeExpiration)))
                .signWith(getSignatureKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    //validar el token de acceso
    public boolean isTokenValid(String token){
        try {
            Jwts.parser()
                    .setSigningKey(getSignatureKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        }catch (Exception e){
            new RuntimeException("TOKEN INVALIDO: ".concat(e.getMessage()));
            return false;
        }
    }

    /*
    * Explicación de los 3 últimos métodos, ya que trabajan juntos
    * 1. extractAllClaims: me trae todos los claims que tiene el token, como el username o información que estoy guardando
    * 2. getClaims: el metodo recibe una función que extrae un solo valor del conjunto de claims, gracias al metodo "apply"
    * 3. getUsernameFromToken: utilizando el getClaims, le pasamos una función definiendo que campo queremos obtener, ejemplo "getSubject"
    *
    * *Nota: Otra forma de obtener la información*
    * - return getClaims(token, claims -> claims.get("email", String.class));
    * */

    //Obtener el username del claims
    public String getUsernameFromToken(String token){
        //Date date_updated = getClaims(token, Claims::getIssuedAt); //extrayendo la fecha de creación que tiene el token, ejemplo de muestra
        return getClaims(token, Claims::getSubject); //extrayendo el usuario del token
    }

    //Obtener un solo Claims
    public <T> T getClaims(String token, Function<Claims, T> claimsTFunction){
        /*
         * <T> T - indica que va retorna cualquier tipo de elemento que me puede traer el claims
         * Function<Claims, T> claimsTFunction - Se trata de una función que toma el Claims completo y devuelve un único valor del tipo T
         * apply - es el metodo que utilizo para indicar que me traiga todos los datos como el username, email, expiración, etc y ya yo busco el que necesite
         */
        Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    //Obtener todos los claims del token(que sería el usuario que está guardado y sus más datos que tiene el token)
    public Claims extractAllClaims(String token){
        return Jwts.parser()
                .setSigningKey(getSignatureKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}