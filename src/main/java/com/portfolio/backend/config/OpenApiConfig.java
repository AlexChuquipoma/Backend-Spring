package com.portfolio.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CiberPortfolio API")
                        .version("1.0.0")
                        .description("API REST del proyecto CiberPortfolio - Plataforma de portafolio y asesorías para programadores. "
                                + "Desarrollado con Spring Boot 4, Java 21, PostgreSQL, Cloudinary y Brevo.")
                        .contact(new Contact()
                                .name("Alexander Chuquipoma & Juan Fernandez")
                                .email("achuquipoma@est.ups.edu.ec")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Ingresa tu token JWT. Obtenlo en /api/auth/login")));
    }
}
