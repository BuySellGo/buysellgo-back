package com.buysellgo.userservice.common.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@OpenAPIDefinition
@Configuration
public class SwaggerConfig {
//    private final static String AUTH_HEADER = "Authorization";
//
//    @Bean
//    public OpenAPI openApi() {
//        var securityScheme = new SecurityScheme()
//                .type(SecurityScheme.Type.HTTP)
//                .scheme("bearer")
//                .bearerFormat(AUTH_HEADER)
//                .in(SecurityScheme.In.HEADER)
//                .name(AUTH_HEADER);
//
//        var addSecurityItem = new SecurityRequirement();
//        addSecurityItem.addList(AUTH_HEADER);
//
//        return new OpenAPI()
//                .components(new Components().addSecuritySchemes(AUTH_HEADER, securityScheme))
//                .addSecurityItem(addSecurityItem)
//                .info(apiInfo());
//    }
//
//    private Info apiInfo() {
//        return new Info()
//                .title("User Service")
//                .description("User Service API")
//                .version("v1");
//    }


    @Bean
    public OpenAPI customOpenApi(@Value("${openapi.service.url}") String url,
                                 @Value("${openapi.service.title}") String serviceTitle,
                                 @Value("${openapi.service.version}") String serviceVersion
                                 ) {

                return new OpenAPI()
                        .servers(List.of(new Server().url(url)))
                        .components(new Components().addSecuritySchemes(
                            "Bearer",
                            new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
                        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                        .info(new Info()
                                .title(serviceTitle)
                                .version(serviceVersion)
                                .description("User Service API"));
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
