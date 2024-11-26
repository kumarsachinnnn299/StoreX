package com.ecommerce.project.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI baseOpenAPI(){
        return new OpenAPI().components(new Components())
                .info(new Info()
                        .title("Spring Boot E-Commerce Application Backend ")
                        .version("1.0.0").description("Here we have defined all the REST API's for our " +
                                "E-Commerce Application:\n " +
                                "You should firstly signup and then signin using the credentials " +
                                "to test all the endpoints"));

    }
}
