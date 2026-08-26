package com.dev.mtrs.projects.qualifyguruv2.infrastructure.docs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI qualifyGuruOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info().title("Qualify Guru V2 API")
                        .description("Automated AI-driven resume qualification and candidate management API.")
                        .version("v1.0.0")
                        .contact(new Contact().name("Qualify Guru Engineering").email("support@qualifyguru.com")))

                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))

                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("For Swagger UI testing, copy the JWT from your login response (if configured to return it) or extract it from network tabs and paste it here. Mobile clients use this header. Browsers use the HttpOnly cookie automatically.")));
    }
}
