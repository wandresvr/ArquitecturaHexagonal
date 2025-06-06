# Sistema de Gestión de Pedidos de Restaurante

### Wilson Andrés Vargas Rojas
### Yeison Fabian Suarez Alba

# Contexto:
Una cadena de restaurantes necesita un sistema para gestionar pedidos en línea.
El sistema debe manejar la creación de pedidos, gestionar el inventario de
ingredientes y facilitar la comunicación entre el área de recepción de pedidos y la
cocina.

# Descripción
Este proyecto es una implementación de dos microservicios para la gestión de pedidos y el inventario asociado, cada microservicio está desarrollado con Java Spring Boot, siguiendo los principios de una arquitectura limpia y hexagonal para fomentar una separación clara entre las capas de dominio, infraestructura y aplicación. Adicionalmente usa un broker de mensajería para la comunicación asicrona de los microservicios.

 ### Order
Es el microservicio encargado de la gestión de pedidos.

### Stock
Es el microservicio encargado de la gestión del inventario.


## Flujo de trabajo
- Un cliente quiere realizar un pedido, este selecciona los productos que quiere y además de pedirsele (Obtener) su información personal e información de entrega (Como la dirección).
- Al enviarse el pedido se valida que los productos eelegidos contengan una receta y que a su vez los ingredientes de la receta se encuentren disponibles al igual que sus cantidades.
- En caso de que no exista la receta, no existan los ingredientes o no haya suficiente cantidad de ingredientes para prepararla se rechaza el pedido.
- En caso de que exista la receta y estén disponibles sus ingredientes como sus cantidades, se acepta el pedido y se procede a disminuir la cantidad de los ingredientes en inventario según la cantidad necesaria en cada receta.

``` mermaid
sequenceDiagram
    participant Cliente
    participant Order
    participant Stock
    participant Broker

    Cliente->>Order: Hacer pedido
    Order->>Broker: ¿Tiene receta y stock?
    
    alt 
        Broker->>Stock: Verificar
        Stock-->>Broker: Sí (con ingredientes y receta)
        Broker->>Order: Aprobar
        Order->>Stock: Quitar ingredientes
        Order->>Cliente: Pedido con stock
    else Algo falta
        Broker->>Stock: Verificar
        Stock-->>Broker: No (Sin receta o ingredientes)
        Broker->>Order: Rechazar
        Order->>Cliente: Pedido sin stock
    end
```


## Implementaciones y dependecias

### Lombok
El proyecto utiliza Lombok, una biblioteca de Java que ayuda a reducir el código repetitivo y hacer el código más limpio y mantenible. Con Lombok, se automatiza la generación de métodos comunes. Esto mejora la productividad y la legibilidad del código.

### Common
Commmon es una librería creada para facilitar la comunicación entre los dos microservicios en cuanto a consistencia de datos se refiere, su proposito es la de evitar código repetitivo en cada microservicio.

### PostgresSQL
El proyecto usa PostgresSQL como base de datos, cada microservicio tiene una y estan definidas desde la capa de infraestructura. En el archivo ``` aplication.properties ``` de cada microservicio se encuentran los parámetros de configuración de la conexión.

### Docker 
Tanto los microservicios, como algunas herramientas, se encuentran en contendores para su fácil implementación y despliegue.

### RabbitMQ
El proyecto usa RabbitMQ como broker de mensajería para la comunicación entre microservcios, definido en la capa de infraestructura, también se cuenta con un pequeño contendor de [docker](rabbitmq) para su rápido despliegue.

## Calidad y pruebas.

### Mockito
Mockea los servicios para hacer implementaciones de las APIs

### Jacoco
Se utiliza para generar un reporte de cobertura de código hecho por las pruebas.

### SonarQube
Analizador de código estatico con el que se validan code smells, brechas de seguridad, duplicidad de código y cobertura de pruebas. Se encuentra un contenedor de [docker](sonarqube) para su fácil uso.

# Despliegue

Para un fácil despliegue se ha realizado algunos scripts.

### Puertos
Para que los microservicios puedan funcionar se tiene un [script](config) que abre los puertos necesarios para hacer los llamados a API de estos.

### Contenedores
Se automatizó un [script](config) para iniciar, reiniciar, detener y recompilar los contendores de los microservicios y su broker.

### Base de datos.
Se realizó un [script](database) que pobla las bases de datos con algo de información, al igual que otro para realizar un backup de información.

### OpenAPI
Se cuenta con un pequeño swagger para la implementación rápida de los endpoints de los microservicios.

<br>
 
# Tabla de contenido
Se realiza una pequeña tabla de contenido para ver algunas especificaciones de los microservicios y scripts

1. [Gestión de pedidos](order)
  * Estructura
  * Diagrama del dominio
  * Headers
  * Endpoints
  * Lanzamiento del docker
  * Ajustes del docker
  * Casos de prueba
2. [Gestión de inventarios](stock)
  * Estructura
  * Diagrama del dominio
  * Headers
  * Endpoints
  * Lanzamiento del docker
  * Ajustes del docker
  * Casos de prueba
3. [Configuración](config)
  * Abrir puertos
  * Gestionar contenedores 
4. [Base de datos](database)
  * Poblar datos
  * Realizar backup