package com.onclass.persona.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI personaApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Persona API")
                        .description("Gestionar personas")
                        .version("1.0.0"));
    }
}
