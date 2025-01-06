package com.buysellgo.promotionservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RestController
public class HelloPromotionService {

    @GetMapping("/hello-promotion-service")
    public String helloPromotionService(){
        return "Hello Promotion Service ~~";
    }

}
