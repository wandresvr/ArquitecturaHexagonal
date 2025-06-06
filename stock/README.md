# Microservicio gestion de inventario
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


# Estructura:

```plaintext

├── order/src/
│ ├── main/
│ │ ├── java/com/wilson/stock/
│ │ │ ├── application/
│ │ │ │ ├── dto/
│ │ │ │ ├── mapper/
│ │ │ │ └── services/
│ │ │ │ ├── ports/
│ │ │ │ │ └── inputs/
│ │ │ │ │ └── outputs/
│ │ │ ├── domain/
│ │ │ │ ├── entitites/
│ │ │ │ ├── exception/
│ │ │ │ ├── repository/
│ │ │ │ └── valueobjects/
│ │ │ ├── infrastructure/
│ │ │ │ ├── config/
│ │ │ │ ├── mapper/
│ │ │ │ ├── messaging/
│ │ │ │ ├── persistence/
│ │ │ │ │ ├── adapter/
│ │ │ │ │ ├── base/
│ │ │ │ │ ├── dto/
│ │ │ │ │ ├── entity/
│ │ │ │ │ ├── mapper/
│ │ │ │ │ ├── repository/
│ │ │ │ │ ├── valueobjects/
│ │ │ │ ├── api/
│ │ │ │ │ ├── config/
│ │ │ │ │ ├── controllers/
│ │ │ │ │ ├── dto/
│ │ │ │ │ ├── exception/
│ │ │ │ │ ├── mapper/
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

# Headers
``` Content-Type: application/json ```


# Endpoints

### Recetas

#### Crear una nueva receta
```http
POST /api/v1/recipes
```

##### Parámetros

| Nombre | Tipo | Requerido | Descripción |
|--------|------|-----------|-------------|
| recipeName | String | Sí | Nombre de la receta |
| ingredients | Array | Sí | Lista de ingredientes |
| ingredientId | UUID | Sí | ID del ingrediente |
| quantity | Float | Sí | Cantidad del ingrediente |
| unit | String | Sí | Unidad de medida |

##### Ejemplo de Request
```json
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

#### Obtener todas las recetas
```http
GET /api/v1/recipes
```

##### Respuesta
```json
[
    {
        "id": "123e4567-e89b-12d3-a456-426614174000",
        "recipeName": "Pan",
        "ingredients": [
            {
                "ingredientId": "123e4567-e89b-12d3-a456-426614174000",
                "quantity": 500.0,
                "unit": "gramos"
            }
        ]
    }
]
```

#### Obtener una receta por ID
```http
GET /api/v1/recipes/{id}
```

##### Parámetros de Path

| Nombre | Tipo | Requerido | Descripción |
|--------|------|-----------|-------------|
| id | UUID | Sí | ID de la receta |

#### Actualizar una receta
```http
PUT /api/v1/recipes/{id}
```

##### Parámetros de Path

| Nombre | Tipo | Requerido | Descripción |
|--------|------|-----------|-------------|
| id | UUID | Sí | ID de la receta |

##### Parámetros de Request

| Nombre | Tipo | Requerido | Descripción |
|--------|------|-----------|-------------|
| recipeName | String | Sí | Nombre de la receta |
| ingredients | Array | Sí | Lista de ingredientes |
| ingredientId | UUID | Sí | ID del ingrediente |
| quantity | Float | Sí | Cantidad del ingrediente |
| unit | String | Sí | Unidad de medida |

##### Ejemplo de Request
```json
{
    "recipeName": "Pan Integral",
    "ingredients": [
        {
            "ingredientId": "123e4567-e89b-12d3-a456-426614174000",
            "quantity": 400.0,
            "unit": "gramos"
        }
    ]
}
```

#### Eliminar una receta
```http
DELETE /api/v1/recipes/{id}
```

##### Parámetros de Path

| Nombre | Tipo | Requerido | Descripción |
|--------|------|-----------|-------------|
| id | UUID | Sí | ID de la receta |

### Ingredientes

#### Crear un nuevo ingrediente
```http
POST /api/v1/ingredients
```

##### Parámetros

| Nombre | Tipo | Requerido | Descripción |
|--------|------|-----------|-------------|
| name | String | Sí | Nombre del ingrediente |
| description | String | Sí | Descripción del ingrediente |
| quantity | Float | Sí | Cantidad disponible |
| unit | String | Sí | Unidad de medida |

##### Ejemplo de Request
```json
{
    "name": "Harina",
    "description": "Harina de trigo",
    "quantity": 1000,
    "unit": "gramos"
}
```

#### Obtener todos los ingredientes
```http
GET /api/v1/ingredients
```

##### Respuesta
```json
[
    {
        "id": "123e4567-e89b-12d3-a456-426614174000",
        "name": "Harina",
        "description": "Harina de trigo",
        "quantity": 1000.0,
        "unit": "gramos"
    }
]
```

#### Obtener un ingrediente por ID
```http
GET /api/v1/ingredients/{id}
```

##### Parámetros de Path

| Nombre | Tipo | Requerido | Descripción |
|--------|------|-----------|-------------|
| id | UUID | Sí | ID del ingrediente |

#### Actualizar un ingrediente
```http
PUT /api/v1/ingredients/{id}
```

##### Parámetros de Path

| Nombre | Tipo | Requerido | Descripción |
|--------|------|-----------|-------------|
| id | UUID | Sí | ID del ingrediente |

##### Parámetros de Request

| Nombre | Tipo | Requerido | Descripción |
|--------|------|-----------|-------------|
| name | String | Sí | Nombre del ingrediente |
| description | String | Sí | Descripción del ingrediente |
| quantity | Float | Sí | Cantidad disponible |
| unit | String | Sí | Unidad de medida |

##### Ejemplo de Request
```json
{
    "name": "Harina Integral",
    "description": "Harina de trigo integral",
    "quantity": 800,
    "unit": "gramos"
}
```

#### Eliminar un ingrediente
```http
DELETE /api/v1/ingredients/{id}
```

##### Parámetros de Path

| Nombre | Tipo | Requerido | Descripción |
|--------|------|-----------|-------------|
| id | UUID | Sí | ID del ingrediente |

## Códigos de Respuesta

| Código | Descripción |
|--------|-------------|
| 200 | OK |
| 201 | Creado |
| 400 | Solicitud inválida |
| 404 | No encontrado |
| 500 | Error interno del servidor |

## Ejemplos de Uso

### Usando cURL

```bash
# Crear una receta
curl -X POST http://localhost:8081/api/v1/recipes \
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

# Obtener todas las recetas
curl -X GET http://localhost:8081/api/v1/recipes

# Obtener una receta específica
curl -X GET http://localhost:8081/api/v1/recipes/123e4567-e89b-12d3-a456-426614174000

# Actualizar una receta
curl -X PUT http://localhost:8081/api/v1/recipes/123e4567-e89b-12d3-a456-426614174000 \
-H "Content-Type: application/json" \
-d '{
    "recipeName": "Pan Integral",
    "ingredients": [
        {
            "ingredientId": "123e4567-e89b-12d3-a456-426614174000",
            "quantity": 400,
            "unit": "gramos"
        }
    ]
}'

# Eliminar una receta
curl -X DELETE http://localhost:8081/api/v1/recipes/123e4567-e89b-12d3-a456-426614174000

# Crear un ingrediente
curl -X POST http://localhost:8081/api/v1/ingredients \
-H "Content-Type: application/json" \
-d '{
    "name": "Harina",
    "description": "Harina de trigo",
    "quantity": 1000,
    "unit": "gramos"
}'

# Obtener todos los ingredientes
curl -X GET http://localhost:8081/api/v1/ingredients

# Obtener un ingrediente específico
curl -X GET http://localhost:8081/api/v1/ingredients/123e4567-e89b-12d3-a456-426614174000

# Actualizar un ingrediente
curl -X PUT http://localhost:8081/api/v1/ingredients/123e4567-e89b-12d3-a456-426614174000 \
-H "Content-Type: application/json" \
-d '{
    "name": "Harina Integral",
    "description": "Harina de trigo integral",
    "quantity": 800,
    "unit": "gramos"
}'

# Eliminar un ingrediente
curl -X DELETE http://localhost:8081/api/v1/ingredients/123e4567-e89b-12d3-a456-426614174000
``` 




# Swagger

El Swagger es una documentación más detallada de los endpoints, para acceder a ella el microservicio debe estar corriendo:

http://localhost:8081/swagger-ui/index.htm


# Lanzamiento del docker

### Iniciar tanto el servicio como la base de datos
``` sh
docker-compose up
```

### Notas:
- Docker debe estar instalado
- El servicio corre en el puerto 8081

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