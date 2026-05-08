package com.hireflow.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("HireFlow Interview Management API")
                        .version("1.0")
                        .description(
                                "A full-stack interview scheduling platform that enables " +
                                        "candidates, interviewers, and recruiters to manage hiring workflows."
                        )
                        .contact(new Contact()
                                .name("HireFlow Team")
                                .email("support@hireflow.com"))
                        .license(new License()
                                .name("MIT License")));
    }
}