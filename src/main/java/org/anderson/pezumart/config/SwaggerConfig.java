package org.anderson.pezumart.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("PezuMart API")
                         .version("1.0")
                        .termsOfService("http://swagger.io/terms/")
                        .description("API para el manejo de productos y usuarios")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
