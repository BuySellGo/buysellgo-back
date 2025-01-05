package com.buysellgo.statisticsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class StatisticsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StatisticsServiceApplication.class, args);
    }

}
