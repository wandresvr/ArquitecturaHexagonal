package com.itm.edu.order.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    private final Environment environment;

    public OpenApiConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public OpenAPI orderOpenAPI() {
        String serverUrl = environment.getProperty("swagger.server.url", "http://localhost");
        String baseUrl = serverUrl + ":" + serverPort;
        
        return new OpenAPI()
                .info(new Info()
                        .title("Order Service API")
                        .description("API para el servicio de gestión de órdenes")
                        .version("1.0")
                        .contact(new Contact()
                                .name("ITM")
                                .email("wilson.vargas@itm.edu.co")
                                .url("https://github.com/wilsonvargas"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server()
                                .url(baseUrl)
                                .description("Servidor de producción")
                ));
    }
} 