package com.itm.edu.stock.infrastructure.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.port:8081}")
    private String serverPort;

    @Value("${swagger.server.url:http://localhost}")
    private String serverUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        String baseUrl = serverUrl + ":" + serverPort;
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestión de Stock")
                        .version("1.0")
                        .description("""
                            API para la gestión de inventario y recetas.
                            
                            ## Características
                            - Gestión de recetas
                            - Gestión de ingredientes
                            - Control de inventario
                            - Integración con RabbitMQ
                            
                            ## Endpoints Principales
                            - `/api/v1/recipes`: Gestión de recetas
                            - `/api/v1/ingredients`: Gestión de ingredientes
                            
                            ## Autenticación
                            No se requiere autenticación para acceder a la API.
                            
                            ## Respuestas
                            Todas las respuestas incluyen:
                            - Código de estado HTTP
                            - Mensaje descriptivo
                            - Datos de la respuesta (si aplica)
                            
                            ## Errores
                            Los errores se devuelven con:
                            - Código de estado HTTP apropiado
                            - Mensaje de error descriptivo
                            - Timestamp del error
                            """)
                        .contact(new Contact()
                                .name("ITM")
                                .email("contacto@itm.edu.co")
                                .url("https://www.itm.edu.co"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server()
                                .url(baseUrl)
                                .description("Servidor de producción")
                ))
                .tags(List.of(
                        new Tag()
                                .name("Recetas")
                                .description("Operaciones relacionadas con la gestión de recetas"),
                        new Tag()
                                .name("Ingredientes")
                                .description("Operaciones relacionadas con la gestión de ingredientes")
                ));
    }
} 