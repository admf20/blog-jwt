package com.andres.blog_jwt_mysql.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class TesController {

    @GetMapping("/home")
    public String homePublic(){
        return "Pagina Publica";
    }
}
