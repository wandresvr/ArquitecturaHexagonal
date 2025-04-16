

# API Gestion de pedidos
Esta desarrollada usando Sprint Booot, Gradle y Lombok, usa una arquitecura hexagonal. 

# Contenido

* Estructura
* Diagrama del dominio
* Headers
* Endpoints
  * Parámetros
  * Ejemplo body
  * Ejemplo usando Curl
  * Notas
* Lanzamiento del docker
  * Construir la imagen
  * Ejecutar el docker
  * Iniciar tanto el servicio como la base de datos
* Ajustes del docker
  * Variables de entorno de PostgreSQL:
  * Versión de PostgreSQL:
* Casos de prueba
  * Ejecución
  * Cobertura


## Estructura:

```plaintext

├── order/
│ ├── src/main/java/com/wilson/order/
│ │ ├── application/
│ │ │ ├── ports/
│ │ │ │ └── inputs/
│ │ │ │ └── outputs/
│ │ │ └── services/
│ │ ├── domain/
│ │ │ ├── exception/
│ │ │ ├── model/
│ │ │ ├── repository/
│ │ │ └── valueobjects/
│ │ └── infrastructure/
│ │ │ ├── config/
│ │ │ ├── rest/
│ │ │ │ ├── dto/
│ │ │ │ ├── mapper/
│ │ │ └── persistence/

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

## Headers
``` Content-Type: application/json ```

# Endpoints
  
## 1. Ingresar Ordenes de pedido
``` POST /api/orders ```

### Parámetros:

- client (Dictionary): Información del cliente con:
  - name (String): Nombre del cliente
  - email (String): Correo del cliente
  - phone (String): Teléfono del cliente
- products (Array): Lista de productos en la orden con:
  - productId (UUID): Identificador único del producto.
  - quantity (Number): Cantidad de ese producto.
- shippingAddress (Dictionary): Información de envio con:
  - street (String): Dirección
  - city (String): Ciudad
  - state (String): Departamento/estado/provincia
  - zipCode (String): Código postal
  - country (String): País

### Ejemplo Body:
``` json
{
    "client": {
        "name": "Juan Pérez",
        "email": "juan@example.com",
        "phone": "1234567890"
    },
    "products": [
        {
            "productId": "123e4567-e89b-12d3-a456-426614174000",
            "quantity": 2
        }
    ],
    "shippingAddress": {
        "street": "123 Main St",
        "city": "New York",
        "state": "NY",
        "zipCode": "10001",
        "country": "USA"
    }
}
```

### Ejemplo usando curl:
``` sh
curl -X POST http://localhost:8080/api/orders \
-H "Content-Type: application/json" \
-d '{
    "client": {
        "name": "Juan Pérez",
        "email": "juan@example.com",
        "phone": "1234567890"
    },
    "products": [
        {
            "productId": "123e4567-e89b-12d3-a456-426614174000",
            "quantity": 2
        }
    ],
    "shippingAddress": {
        "street": "123 Main St",
        "city": "New York",
        "state": "NY",
        "zipCode": "10001",
        "country": "USA"
    }
}'
```

### Notas:

- Se debe tener productos en la base de datos creados antes de crear ordenes.
- La relación entre la orden y los productos se encuentran en la tabla ```Total_Order```.


## 2. Crear un producto
  ``` POST /api/product ```

### Parámetros:

- name (String): Nombre del producto
- description (String): Descripción del producto
- price (Float): Precio del producto por unidad
- stock (Int): Cantidad del producto en stock


### Ejemplo Body:

``` json
{
    "name": "Pizza Margarita",
    "description": "Pizza clásica italiana",
    "price": 15.99,
    "stock": 50
}
```

### Ejemplo usando curl:

``` sh
curl -X POST http://localhost:8080/api/products \
-H "Content-Type: application/json" \
-d '{
    "name": "Pizza Margarita",
    "description": "Pizza clásica italiana",
    "price": 15.99,
    "stock": 50
}'
```

## 3. Obtener un producto
``` GET /api/product/{product} ```

### Parámetros:
{product} : id del producto

### Ejemplo usando curl:

``` sh
curl -X GET http://localhost:8080/api/products/123e4567-e89b-12d3-a456-426614174000
```

### Notas:
- Para obtener todos los productos sólo se debe omitir el parametro

Ejemplo:
``` sh
curl -X GET http://localhost:8080/api/products/
```

## Lanzamiento del docker

### Construir la imagen
Primero se debe construir la imagen con:
``` sh
docker build -t order-service .
```

### Ejecutar el docker
``` sh
docker run -p 8080:8080 order-service
```

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

## Casos de prueba

### Ejecución
Para ejecutar los casos de prueba solo se debe ejecutar:
``` sh
./gradlew test
```

### Cobertura
Para validar la cobertura se debe ejecutar:
``` sh
./gradlew clean test jacocoTestReport
```

se creará la carpeta [coverage](coverage), para ver la cobertura abra el archivo de [reporte](coverage/html/index.html) (index.html) dentro de un navegador