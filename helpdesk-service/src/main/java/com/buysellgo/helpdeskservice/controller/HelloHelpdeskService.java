package com.buysellgo.helpdeskservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RestController
public class HelloHelpdeskService {

    @GetMapping("/hello-helpdesk-service")
    public String helloHelpdeskService(){
        return "Hello helpdesk Service ~~";
    }

}
