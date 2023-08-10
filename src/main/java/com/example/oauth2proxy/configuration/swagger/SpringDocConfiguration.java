package com.example.oauth2proxy.configuration.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.servers.ServerVariable;
import io.swagger.v3.oas.models.servers.ServerVariables;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SpringDocConfiguration {

    @Value("${openapi.oauth2-proxy.title:${spring.application.name}}")
    String title;

    @Value("${openapi.oauth2-proxy.description:API Specification for OAuth2 Proxy Service}")
    String description;

    @Value("${openapi.oauth2-proxy.base-path:/oauth2-proxy}")
    String basePath;

    @Bean(name = "SpringDocConfiguration.OAuth2Proxy.apiInfo")
    OpenAPI apiInfo() {
        OpenAPI openAPI = new OpenAPI()
                .info(
                        new Info()
                                .title(title)
                                .description(description)
                                .version("1.0.0")
                                .license(
                                        new License()
                                                .name("Apache 2.0")
                                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")
                                )

                )
                .components(
                        new Components()
                                .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                )
                );

        openAPI.addServersItem(
                new Server()
                        .url("{protocol}://{domain}{basePath}")
                        .variables(
                                new ServerVariables()
                                        .addServerVariable("protocol",
                                                new ServerVariable()
                                                        ._default("https")
                                                        ._enum(Arrays.asList("http", "https"))
                                        )
                                        .addServerVariable("domain",
                                                new ServerVariable()
                                                        ._default("localhost:{port}")
                                        )
                                        .addServerVariable("port",
                                                new ServerVariable()
                                                        ._default("443")
                                        )
                                        .addServerVariable("basePath",
                                                new ServerVariable()
                                                        ._default(basePath)
                                        )
                        )
        );
        return openAPI;
    }
}