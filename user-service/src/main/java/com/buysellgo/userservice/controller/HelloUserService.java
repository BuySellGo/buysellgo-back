package com.buysellgo.userservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RestController
public class HelloUserService {

    @GetMapping("/hello-user-service")
    public String helloUserService(){
        return "Hello User Service ~~";
    }

}
