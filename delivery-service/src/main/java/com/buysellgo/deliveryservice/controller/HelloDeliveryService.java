package com.buysellgo.deliveryservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RestController
public class HelloDeliveryService {

    @GetMapping("/hello-delivery-service")
    public String helloDeliveryService(){
        return "Hello delivery Service ~~";
    }

}
