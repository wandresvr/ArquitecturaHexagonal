# API Gestion de inventario
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

├── order/src/
│ ├── main/
│ │ ├── java/com/wilson/stock/
│ │ │ ├── application/
│ │ │ │ ├── ports/
│ │ │ │ │ └── inputs/
│ │ │ │ │ └── outputs/
│ │ │ │ └── services/
│ │ │ ├── domain/
│ │ │ │ ├── entitites/
│ │ │ │ ├── repository/
│ │ │ │ └── valueobjects/
│ │ │ ├── infrastructure/api
│ │ │ │ ├── dto/
│ │ │ │ ├── exception/
│ ├── test/
├── coverage/


```

## Diagrama UML del Dominio:

``` mermaid
classDiagram
    class Recipe {
        -UUID id
        -String name
        -String description
        -List<RecipeIngredient> ingredients
        +addIngredient(Ingredient, BigDecimal, String)
    }
    
    class Ingredient {
        -UUID id
        -String name
        -String description
        -Quantity quantity
        -Unit unit
    }
    
    class RecipeIngredient {
        -UUID id
        -Recipe recipe
        -Ingredient ingredient
        -Quantity quantity
        -Unit unit
    }
    
    class Quantity {
        -BigDecimal value
    }
    
    class Unit {
        -String value
    }
    
    Recipe "1" -- "*" RecipeIngredient
    RecipeIngredient "*" -- "1" Ingredient
    RecipeIngredient -- Quantity
    RecipeIngredient -- Unit
    Ingredient -- Quantity
    Ingredient -- Unit
```

## Headers
``` Content-Type: application/json ```

# Endpoints
  
## 1. Crear una nueva receta
``` POST /api/stock ```

### Parámetros:

- recipeNme (String): Nombre de la receta
- ingredients (Array): Array de ingredientes con:
  - ingredientId (UUID): Identificador único del ingrediente
  - quantity (Float): Cantidad del ingrediente
  - unit (String): Tipo de unidad del ingrediente

### Ejemplo Body:
``` json
{
    "recipeName": "Pan",
    "ingredients": [
        {
            "ingredientId": "123e4567-e89b-12d3-a456-426614174000",
            "quantity": 500.0,
            "unit": "gramos"
        }
    ]
}
```

### Ejemplo usando curl:
``` sh
curl -X POST http://localhost:8080/api/stock/recipes \
-H "Content-Type: application/json" \
-d '{
    "recipeName": "Pan",
    "ingredients": [
        {
            "ingredientId": "123e4567-e89b-12d3-a456-426614174000",
            "quantity": 500,
            "unit": "gramos"
        }
    ]
}'
```

### Notas:

- Se debe tener ingredientes en la base de datos creados antes de crear una receta.
- La relación entre la receta y los ingredientes se encuentran en la tabla ```recipe_ingredients```.


## 2. Crear un ingrediente
  ``` POST /api/ingredient ```

### Parámetros:

- name (String): Nombre del ingrediente
- description (String): Descripción del ingrediente
- quantity (Float): Cantidad del producto
- unit (String): Tipo de unidad


### Ejemplo Body:

``` json
{
    "name": "Harina",
    "description": "Harina de trigo",
    "quantity": 1000,
    "unit": "gramos"
}
```

### Ejemplo usando curl:

``` sh
curl -X POST http://localhost:8080/api/stock/ingredients \
-H "Content-Type: application/json" \
-d '{
    "name": "Harina",
    "description": "Harina de trigo",
    "quantity": 1000,
    "unit": "gramos"
}'
```

## 3. Obtener un ingrediente
``` GET /api/product/{ingredient} ```

### Parámetros:
{ingredient} : id del ingrediente

### Ejemplo usando curl:

``` sh
curl -X GET http://localhost:8080/api/stock/ingredients/123e4567-e89b-12d3-a456-426614174000
```

### Notas:
- Para obtener todos los ingredientes sólo se debe omitir el parametro.

Ejemplo:
``` sh
curl -X GET http://localhost:8080/api/stock/ingredients/
```

## Lanzamiento del docker

### Construir la imagen
Primero se debe construir la imagen con:
``` sh
docker build -t stock-service .
```

### Ejecutar el docker
``` sh
docker run -p 8080:8080 stock-service
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

se creará la carpeta [coverage](coverage), para ver la cobertura abra el archivo de [reporte](coverage/html/index.html) (index.html) dentro de un navegador.