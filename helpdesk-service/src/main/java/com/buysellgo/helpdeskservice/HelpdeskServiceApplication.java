package com.buysellgo.helpdeskservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class HelpdeskServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelpdeskServiceApplication.class, args);
    }

}
