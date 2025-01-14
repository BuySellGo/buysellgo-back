package com.buysellgo.helpdeskservice.common.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
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

        System.out.println("!@#$%^&*()_!@#$%^&*()!@#$%^&*(@#$%^&*(@#$%^&*(!@#$%^&*(@#$%^&*");
        log.info("serverUrl={}", url);

        // Security Scheme 정의
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        // Security Requirement 정의
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearer");

        return new OpenAPI()
                .servers(List.of(new Server().url(url)))
                .info(new Info()
                        .title(serviceTitle)
                        .version(serviceVersion)
                        .description("Help Desk Service API"))
                .addSecurityItem(securityRequirement)
                .schemaRequirement("BearerAuth", securityScheme);
    }

//    @Bean
//    public OpenAPI openAPI(){
//        Info info = new Info()
//                .title("User Service")
//                .version("v1")
//                .description("User Service API");
//
//        SecurityScheme jwtSecurityScheme = new SecurityScheme()
//                .type(SecurityScheme.Type.HTTP)
//                .scheme("Bearer")
//                .bearerFormat("JWT")
//                .in(SecurityScheme.In.HEADER)
//                .name("Authorization");
//
//        SecurityScheme xAuthorizationId = new SecurityScheme()
//                .type(SecurityScheme.Type.APIKEY)
//                .in(SecurityScheme.In.HEADER)
//                .name("X-Authorization-Id");
//
//        SecurityRequirement securityRequirement = new SecurityRequirement()
//                .addList("bearerAuth")
//                .addList("X-Authorization");
//
//        Server server = new Server().url("${openapi.service.url}");
//
//        return new OpenAPI()
//                .info(info)
//                .components(
//                        new Components()
//                                .addSecuritySchemes("bearerAuth", jwtSecurityScheme)
//                                .addSecuritySchemes("xAuthorization", xAuthorizationId)
//                )
//                .security(List.of(SecurityRequirement))
//                .servers(List.of(server));
//    }

}
