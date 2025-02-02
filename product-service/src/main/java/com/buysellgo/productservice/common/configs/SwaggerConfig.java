package com.buysellgo.productservice.common.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
//@OpenAPIDefinition(info = @io.swagger.v3.oas.annotations.info.Info(title="user service", version="v1", description = "Documentation user service api v1.0"))
@OpenAPIDefinition
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenApi(
            @Value("${openapi.service.url}") String url,
            @Value("${openapi.service.title}") String serviceTitle,
            @Value("${openapi.service.version}") String serviceVersion) {

        log.info("serverUrl={}", url);

        return new OpenAPI()
                .servers(List.of(new Server().url(url)))
                .info(new Info()
                        .title(serviceTitle)
                        .version(serviceVersion)
                        .description("Product Service API"));
    }
}
