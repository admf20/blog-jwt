package com.andres.blog_jwt_mysql.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Maneja errores de validación de @Valid en @RequestBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex){
        Map<String, String> erros = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error -> {
                    erros.put(error.getField(), error.getDefaultMessage());
                });

        return new ResponseEntity<>(erros, HttpStatus.BAD_REQUEST);
    }

    // Maneja errores simples (como RuntimeException lanzados manualmente)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntime(RuntimeException ex) {
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("error: ", ex.getMessage());
        return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
    }

    // (Opcional) Manejo de validación de parámetros individuales (ej: @RequestParam)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolations(ConstraintViolationException ex){
        Map<String, String> errors = new HashMap<>();

        ex.getConstraintViolations()
                .forEach(error -> {
                    errors.put("Parámetro: " ,error.getMessage());
                });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
