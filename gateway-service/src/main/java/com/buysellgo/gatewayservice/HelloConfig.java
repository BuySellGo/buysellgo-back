package com.buysellgo.gatewayservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloConfig {

    @Value("${spring.datasource.url}")
    private String url;

    @GetMapping("/api")
    public String api() {
        System.out.println("url = " + url);
        return "깨개갱~";
    }

    @GetMapping("/hello")
    public String hello() {
        System.out.println("url = " + url);

        return "<h1> Hello K8S!!! Update Version </h1>";
    }
}
