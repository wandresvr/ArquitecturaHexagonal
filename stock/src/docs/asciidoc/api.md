# API de Gestión de Stock

## Introducción

Esta API proporciona endpoints para la gestión de inventario y recetas.

## Endpoints

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