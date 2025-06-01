package com.itm.edu.order.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OpenApiConfigTest {

    @Test
    void orderOpenAPI_ShouldHaveCorrectConfiguration() {
        // Arrange
        OpenApiConfig config = new OpenApiConfig();

        // Act
        OpenAPI openAPI = config.orderOpenAPI();

        // Assert
        assertNotNull(openAPI);
        
        // Verificar Info
        Info info = openAPI.getInfo();
        assertNotNull(info);
        assertEquals("Order Service API", info.getTitle());
        assertEquals("API para el servicio de gestión de órdenes", info.getDescription());
        assertEquals("1.0", info.getVersion());
        
        // Verificar Contact
        Contact contact = info.getContact();
        assertNotNull(contact);
        assertEquals("ITM", contact.getName());
        assertEquals("https://github.com/wilsonvargas", contact.getUrl());
        assertEquals("wilson.vargas@itm.edu.co", contact.getEmail());
        
        // Verificar License
        License license = info.getLicense();
        assertNotNull(license);
        assertEquals("Apache 2.0", license.getName());
        assertEquals("http://www.apache.org/licenses/LICENSE-2.0.html", license.getUrl());
        
        // Verificar Servers
        List<Server> servers = openAPI.getServers();
        assertNotNull(servers);
        assertFalse(servers.isEmpty());
        
        Server server = servers.get(0);
        assertNotNull(server);
        assertEquals("http://localhost:8080", server.getUrl());
        assertEquals("Servidor de desarrollo", server.getDescription());
    }
} 