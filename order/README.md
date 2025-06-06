# API Gestion de pedidos
Esta desarrollada usando Sprint Booot, Gradle y Lombok, usa una arquitecura hexagonal. 

# Contenido

* Estructura
* Diagrama UML del dominio
* Headers
* Endpoints
* Swagger
* Lanzamiento del docker
* Ajustes del docker
* Calidad y pruebas


## Estructura:

```plaintext

├── order/src/
│ ├── main/
│ │ ├── java/com/wilson/order/
│ │ │ ├── application/
│ │ │ │ └── services/
│ │ │ │ ├── mapper/
│ │ │ │ ├── dto/
│ │ │ │ │ └── events/
│ │ │ │ ├── ports/
│ │ │ │ │ └── inputs/
│ │ │ │ │ └── outputs/
│ │ │ ├── domain/
│ │ │ │ ├── exception/
│ │ │ │ ├── model/
│ │ │ │ ├── repository/
│ │ │ │ └── valueobjects/
│ │ │ ├── infrastructure/
│ │ │ │ ├── config/
│ │ │ │ ├── exception/
│ │ │ │ ├── messaging/
│ │ │ │ ├── persistence/
│ │ │ │ │ ├── adapter/
│ │ │ │ │ ├── entitites/
│ │ │ │ │ ├── mapper/
│ │ │ │ │ ├── repository/
│ │ │ │ ├── rest/
│ │ │ │ │ ├── dto/
│ │ │ │ │ ├── mapper/
│ ├── test/
├── coverage/


```

## Diagrama UML del Dominio:

``` mermaid
classDiagram
    class Order {
        +UUID orderId
        +List~OrderTotal~ products
        +String orderStatus
        +LocalDateTime orderDate
        +String orderNotes
        +AddressShipping deliveryAddress
        +addProduct(Product, BigDecimal)
        +removeProduct(Product)
        +calculateTotalValue() BigDecimal
    }

    class Product {
        +UUID id
        +String name
        +BigDecimal price
    }

    class OrderTotal {
        +Product product
        +BigDecimal quantity
        +calculateValue() BigDecimal
    }

    class AddressShipping {
        +String street
        +String city
        +String state
        +String zipCode
        +String country
    }

    class Client {
        +String clientId
        +String clientName
        +String clientAddress
        +String clientPhone
        +String clientEmail
        +String clientStatus
    }

    class ProductRepository {
        <<interface>>
        +findById(UUID) Optional~Product~
    }

    class OrderRepository {
        <<interface>>
        +save(Order) Order
    }

    Order "1" *-- "*" OrderTotal : 
    OrderTotal "1" *-- "1" Product : 
    Order "1" *-- "1" AddressShipping : 
    Order "1" *-- "1" Client : 
    ProductRepository <|.. Product : 
    OrderRepository  <|..  Order : 
```

# Headers
``` Content-Type: application/json ```

# Endpoints

### Nota
Se usa Localhost por defecto lanzado desde local, pero en realidad es la IP donde se encuentra el microservicio.


  ## Órdenes

API para la gestión de órdenes

### Obtener todas las órdenes

```http
GET http://localhost:8080/api/v1/orders
```

#### Respuestas

| Código | Descripción |
|--------|-------------|
| 200 | Lista de órdenes obtenida exitosamente |
| 500 | Error interno del servidor |

### Crear una nueva orden

```http
POST http://localhost:8080/api/v1/orders
```

#### Parámetros

| Posición | Nombre | Tipo | Descripción |
|----------|--------|------|-------------|
| body | body | CreateOrderRequest | |

#### Respuestas

| Código | Descripción |
|--------|-------------|
| 200 | Orden creada exitosamente |
| 400 | Solicitud inválida |
| 422 | Error de validación |
| 500 | Error interno del servidor |

### Eliminar una orden

```http
DELETE http://localhost:8080/api/v1/orders/{id}
```

#### Parámetros

| Posición | Nombre | Tipo | Descripción |
|----------|--------|------|-------------|
| path | id | string | |

#### Respuestas

| Código | Descripción |
|--------|-------------|
| 200 | Orden eliminada exitosamente |
| 400 | Solicitud inválida |
| 404 | Orden no encontrada |
| 500 | Error interno del servidor |

### Obtener una orden por su ID

```http
GET http://localhost:8080/api/v1/orders/{id}
```

#### Parámetros

| Posición | Nombre | Tipo | Descripción |
|----------|--------|------|-------------|
| path | id | string | |

#### Respuestas

| Código | Descripción |
|--------|-------------|
| 200 | Orden encontrada |
| 404 | Orden no encontrada |
| 500 | Error interno del servidor |

### Actualizar una orden

```http
PUT http://localhost:8080/api/v1/orders/{id}
```

#### Parámetros

| Posición | Nombre | Tipo | Descripción |
|----------|--------|------|-------------|
| path | id | string | ID de la orden |
| body | body | OrderDto | |

#### Respuestas

| Código | Descripción |
|--------|-------------|
| 200 | Orden actualizada exitosamente |
| 400 | Solicitud inválida |
| 404 | Orden no encontrada |
| 422 | Error de validación |
| 500 | Error interno del servidor |

### Actualizar la dirección de envío de una orden

```http
PUT http://localhost:8080/api/v1/orders/{orderId}/shipping-address
```

#### Parámetros

| Posición | Nombre | Tipo | Descripción |
|----------|--------|------|-------------|
| path | orderId | string | ID de la orden |
| body | body | UpdateShippingAddressRequest | |

#### Respuestas

| Código | Descripción |
|--------|-------------|
| 200 | Dirección de envío actualizada exitosamente |
| 400 | Solicitud inválida |
| 404 | Orden no encontrada |
| 422 | Error de validación |
| 500 | Error interno del servidor |

## Productos

API para la gestión de productos

### Obtener todos los productos

```http
GET http://localhost:8080/api/v1/products
```

#### Respuestas

| Código | Descripción |
|--------|-------------|
| 200 | Lista de productos obtenida exitosamente |
| 500 | Error interno del servidor |

### Crear un nuevo producto

```http
POST http://localhost:8080/api/v1/products
```

#### Parámetros

| Posición | Nombre | Tipo | Descripción |
|----------|--------|------|-------------|
| body | body | ProductDto | |

#### Respuestas

| Código | Descripción |
|--------|-------------|
| 201 | Producto creado exitosamente |
| 400 | Solicitud inválida |
| 409 | Conflicto - Ya existe un producto con ese nombre |
| 422 | Error de validación |
| 500 | Error interno del servidor |

### Eliminar un producto

```http
DELETE http://localhost:8080/api/v1/products/{id}
```

#### Parámetros

| Posición | Nombre | Tipo | Descripción |
|----------|--------|------|-------------|
| path | id | string | |

#### Respuestas

| Código | Descripción |
|--------|-------------|
| 204 | Producto eliminado exitosamente |
| 400 | ID inválido |
| 404 | Producto no encontrado |
| 500 | Error interno del servidor |

### Obtener un producto por ID

```http
GET http://localhost:8080/api/v1/products/{id}
```

#### Parámetros

| Posición | Nombre | Tipo | Descripción |
|----------|--------|------|-------------|
| path | id | string | |

#### Respuestas

| Código | Descripción |
|--------|-------------|
| 200 | Producto encontrado |
| 400 | ID inválido |
| 404 | Producto no encontrado |
| 500 | Error interno del servidor |

### Actualizar un producto

```http
PUT http://localhost:8080/api/v1/products/{id}
```

#### Parámetros

| Posición | Nombre | Tipo | Descripción |
|----------|--------|------|-------------|
| path | id | string | |
| body | body | ProductDto | |

#### Respuestas

| Código | Descripción |
|--------|-------------|
| 200 | Producto actualizado exitosamente |
| 400 | ID inválido o solicitud inválida |
| 404 | Producto no encontrado |
| 409 | Conflicto - Ya existe un producto con ese nombre |
| 422 | Error de validación |
| 500 | Error interno del servidor |


# Swagger

El Swagger es una documentación más detallada de los endpoints, para acceder a ella el microservicio debe estar corriendo:

http://localhost:8080/swagger-ui/index.htm


# Lanzamiento del docker

### Iniciar tanto el servicio como la base de datos
``` sh
docker-compose up
```

### Notas:
- Docker debe estar instalado
- El servicio corre en el puerto 8080

## Ajustes del docker
### Variables de entorno de PostgreSQL:
- Nombre de la base de datos (POSTGRES_DB)
- Usuario de PostgreSQL (POSTGRES_USER)
- Contraseña de PostgreSQL (POSTGRES_PASSWORD)

### Versión de PostgreSQL:
   - Actualmente se usa postgres 15


# Calidad y pruebas
Para validar la cobertura se debe ejecutar:
``` sh
./gradlew clean test jacocoTestReport
```

se creará la carpeta [coverage](coverage), para ver la cobertura abra el archivo de [reporte](coverage/html/index.html) (index.html) dentro de un navegador.

### SobarQube

Se puede hacer uso de SonarQube para leer la cobertura y hacer analisis de código estatico, el token debe ser ajustado en
```sonar.properties```.