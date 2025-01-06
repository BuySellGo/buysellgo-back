package com.buysellgo.statisticsservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RestController
public class HelloStatisticsService {

    @GetMapping("/hello-statistics-service")
    public String helloStatisticsService(){
        return "Hello statistics Service ~~";
    }

}
