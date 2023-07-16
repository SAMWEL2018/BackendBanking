package com.example.transcsystem.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Samwel Wafula
 * Created on July 09, 2023.
 * Time 12:25 PM
 */

@Configuration
public class DocConfig {
    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("COMPULYNX TESTING API DOC")
                        .description("This API has been Developed By SAMWEL WAFULA")
                        .version("API V1")
                        .license(new License().name("Apache 2.0")))
                .externalDocs(new ExternalDocumentation()
                        .description("")
                        .url(""))
                .components(new Components()
                        .addSecuritySchemes("oAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .name("oAuth")
                                .scheme("bearer")
                        )
                        .addParameters("header", new Parameter()
                                .in("header")
                                .schema(new StringSchema())
                                .name("myHeader1").required(true))
                        .addHeaders("myHeader1", new Header()
                                .description("myHeader2 header")
                                .schema(new StringSchema()))
                )
                .addSecurityItem(new SecurityRequirement().addList("oAuth").addList("header"));

    }
}
