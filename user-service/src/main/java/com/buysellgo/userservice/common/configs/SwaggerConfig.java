package com.buysellgo.userservice.common.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    private final static String AUTH_HEADER = "Authorization";

    @Bean
    public OpenAPI openApi() {
        var securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat(AUTH_HEADER)
                .in(SecurityScheme.In.HEADER)
                .name(AUTH_HEADER);

        var addSecurityItem = new SecurityRequirement();
        addSecurityItem.addList(AUTH_HEADER);

        return new OpenAPI()
                .components(new Components().addSecuritySchemes(AUTH_HEADER, securityScheme))
                .addSecurityItem(addSecurityItem)
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("프로젝트 API 명세서")
                .description("프로젝트에서 사용되는 api 사용 메뉴얼")
                .version("v1");
    }

}
