# Sistema de Gestión de Pedidos de Restaurante

### Wilson Andrés Vargas Rojas
### Yeison Fabian Suarez Alba

## Contexto:
Una cadena de restaurantes necesita un sistema para gestionar pedidos en línea.
El sistema debe manejar la creación de pedidos, gestionar el inventario de
ingredientes y facilitar la comunicación entre el área de recepción de pedidos y la
cocina.

- ## APIs creadas con Java Spring Boot y Arquitectura Hexagonal
Este proyecto es una implementación de dos API en la que se ha desarrollado utilizando Java Spring Boot como framework principal, siguiendo los principios de arquitectura hexagonal para fomentar una separación clara entre las capas de dominio, infraestructura y aplicaciones.

- ## Uso de Lombok
El proyecto utiliza Lombok, una biblioteca de Java que ayuda a reducir el código repetitivo y hacer el código más limpio y mantenible. Con Lombok, se automatiza la generación de métodos comunes. Esto mejora la productividad y la legibilidad del código.

- ## Uso de PostgresSQL
El proyecto usa PostgresSQL como base de datos, en el archivo ``` aplication.properties ``` se encuentran los parámetros de configuración de la conexión.

- ## Uso de Docker
El proyecto lanza varios tres contenedores donde se encuentra la APIs y la base de datos.

<br>
 
# Tabla de contenido

1. [API Gestión de pedidos](order)
  * Estructura
  * Diagrama del dominio
  * Endpoints
  * Headers
  * Parámetros
  * Ejemplo body
  * Ejemplo usando Curl
  * Lanzamiento del docker
  * Ajustes del docker
  * Casos de prueba
  * Cobertura
2. [API Gestión de inventarios](stock)
  * Estructura
  * Diagrama del dominio
  * Endpoints
  * Headers
  * Parámetros
  * Ejemplo body
  * Ejemplo usando Curl
  * Lanzamiento del docker
  * Ajustes del docker
  2* Casos de prueba
  * Cobertura
3. [Notas](#notas)
