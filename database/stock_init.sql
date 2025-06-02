-- Crear tabla de recetas si no existe
CREATE TABLE IF NOT EXISTS recipes (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    preparation_time INTEGER, -- en minutos
    difficulty VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Crear tabla de ingredientes si no existe
CREATE TABLE IF NOT EXISTS ingredients (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    unit_measure VARCHAR(50),
    stock_quantity DECIMAL(10,2) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Agregar columnas si no existen
DO $$ 
BEGIN
    -- Agregar columna unit_measure si no existe
    IF NOT EXISTS (
        SELECT 1 
        FROM information_schema.columns 
        WHERE table_name = 'ingredients' 
        AND column_name = 'unit_measure'
    ) THEN
        ALTER TABLE ingredients ADD COLUMN unit_measure VARCHAR(50);
    END IF;

    -- Agregar columna stock_quantity si no existe
    IF NOT EXISTS (
        SELECT 1 
        FROM information_schema.columns 
        WHERE table_name = 'ingredients' 
        AND column_name = 'stock_quantity'
    ) THEN
        ALTER TABLE ingredients ADD COLUMN stock_quantity DECIMAL(10,2) DEFAULT 0;
    END IF;
END $$;

-- Crear tabla de relación receta_ingredientes si no existe
CREATE TABLE IF NOT EXISTS recipe_ingredients (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    recipe_id UUID REFERENCES recipes(id),
    ingredient_id UUID REFERENCES ingredients(id),
    quantity DECIMAL(10,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insertar datos de ejemplo para recetas (solo si no existen)
INSERT INTO recipes (id, name, description, preparation_time, difficulty)
SELECT gen_random_uuid(), 'Pizza Margherita', 'Pizza clásica italiana con tomate y mozzarella', 30, 'Fácil'
WHERE NOT EXISTS (SELECT 1 FROM recipes WHERE name = 'Pizza Margherita');

INSERT INTO recipes (id, name, description, preparation_time, difficulty)
SELECT gen_random_uuid(), 'Pasta Carbonara', 'Pasta con salsa cremosa de huevo y panceta', 25, 'Media'
WHERE NOT EXISTS (SELECT 1 FROM recipes WHERE name = 'Pasta Carbonara');

INSERT INTO recipes (id, name, description, preparation_time, difficulty)
SELECT gen_random_uuid(), 'Ensalada César', 'Ensalada con pollo, crutones y aderezo César', 20, 'Fácil'
WHERE NOT EXISTS (SELECT 1 FROM recipes WHERE name = 'Ensalada César');

-- Insertar datos de ejemplo para ingredientes (solo si no existen)
INSERT INTO ingredients (id, name, unit_measure, stock_quantity)
SELECT gen_random_uuid(), 'Harina', 'gramos', 10000
WHERE NOT EXISTS (SELECT 1 FROM ingredients WHERE name = 'Harina');

INSERT INTO ingredients (id, name, unit_measure, stock_quantity)
SELECT gen_random_uuid(), 'Tomate', 'unidades', 100
WHERE NOT EXISTS (SELECT 1 FROM ingredients WHERE name = 'Tomate');

INSERT INTO ingredients (id, name, unit_measure, stock_quantity)
SELECT gen_random_uuid(), 'Mozzarella', 'gramos', 5000
WHERE NOT EXISTS (SELECT 1 FROM ingredients WHERE name = 'Mozzarella');

INSERT INTO ingredients (id, name, unit_measure, stock_quantity)
SELECT gen_random_uuid(), 'Pasta', 'gramos', 8000
WHERE NOT EXISTS (SELECT 1 FROM ingredients WHERE name = 'Pasta');

INSERT INTO ingredients (id, name, unit_measure, stock_quantity)
SELECT gen_random_uuid(), 'Huevo', 'unidades', 200
WHERE NOT EXISTS (SELECT 1 FROM ingredients WHERE name = 'Huevo');

INSERT INTO ingredients (id, name, unit_measure, stock_quantity)
SELECT gen_random_uuid(), 'Panceta', 'gramos', 3000
WHERE NOT EXISTS (SELECT 1 FROM ingredients WHERE name = 'Panceta');

INSERT INTO ingredients (id, name, unit_measure, stock_quantity)
SELECT gen_random_uuid(), 'Lechuga', 'unidades', 50
WHERE NOT EXISTS (SELECT 1 FROM ingredients WHERE name = 'Lechuga');

INSERT INTO ingredients (id, name, unit_measure, stock_quantity)
SELECT gen_random_uuid(), 'Crutones', 'gramos', 2000
WHERE NOT EXISTS (SELECT 1 FROM ingredients WHERE name = 'Crutones');

INSERT INTO ingredients (id, name, unit_measure, stock_quantity)
SELECT gen_random_uuid(), 'Aceite de oliva', 'ml', 5000
WHERE NOT EXISTS (SELECT 1 FROM ingredients WHERE name = 'Aceite de oliva');

INSERT INTO ingredients (id, name, unit_measure, stock_quantity)
SELECT gen_random_uuid(), 'Sal', 'gramos', 1000
WHERE NOT EXISTS (SELECT 1 FROM ingredients WHERE name = 'Sal');

-- Insertar relaciones receta_ingredientes (solo si no existen)
WITH recipe_ids AS (
    SELECT id FROM recipes ORDER BY name LIMIT 3
),
ingredient_ids AS (
    SELECT id FROM ingredients ORDER BY name LIMIT 10
)
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity)
SELECT 
    (SELECT id FROM recipe_ids OFFSET 0 LIMIT 1), -- Pizza Margherita
    (SELECT id FROM ingredient_ids OFFSET 0 LIMIT 1), -- Harina
    300
WHERE NOT EXISTS (
    SELECT 1 FROM recipe_ingredients 
    WHERE recipe_id = (SELECT id FROM recipe_ids OFFSET 0 LIMIT 1) 
    AND ingredient_id = (SELECT id FROM ingredient_ids OFFSET 0 LIMIT 1)
)
UNION ALL
SELECT 
    (SELECT id FROM recipe_ids OFFSET 0 LIMIT 1),
    (SELECT id FROM ingredient_ids OFFSET 1 LIMIT 1), -- Tomate
    4
WHERE NOT EXISTS (
    SELECT 1 FROM recipe_ingredients 
    WHERE recipe_id = (SELECT id FROM recipe_ids OFFSET 0 LIMIT 1) 
    AND ingredient_id = (SELECT id FROM ingredient_ids OFFSET 1 LIMIT 1)
)
UNION ALL
SELECT 
    (SELECT id FROM recipe_ids OFFSET 0 LIMIT 1),
    (SELECT id FROM ingredient_ids OFFSET 2 LIMIT 1), -- Mozzarella
    200
WHERE NOT EXISTS (
    SELECT 1 FROM recipe_ingredients 
    WHERE recipe_id = (SELECT id FROM recipe_ids OFFSET 0 LIMIT 1) 
    AND ingredient_id = (SELECT id FROM ingredient_ids OFFSET 2 LIMIT 1)
)
UNION ALL
SELECT 
    (SELECT id FROM recipe_ids OFFSET 1 LIMIT 1), -- Pasta Carbonara
    (SELECT id FROM ingredient_ids OFFSET 3 LIMIT 1), -- Pasta
    500
WHERE NOT EXISTS (
    SELECT 1 FROM recipe_ingredients 
    WHERE recipe_id = (SELECT id FROM recipe_ids OFFSET 1 LIMIT 1) 
    AND ingredient_id = (SELECT id FROM ingredient_ids OFFSET 3 LIMIT 1)
)
UNION ALL
SELECT 
    (SELECT id FROM recipe_ids OFFSET 1 LIMIT 1),
    (SELECT id FROM ingredient_ids OFFSET 4 LIMIT 1), -- Huevo
    2
WHERE NOT EXISTS (
    SELECT 1 FROM recipe_ingredients 
    WHERE recipe_id = (SELECT id FROM recipe_ids OFFSET 1 LIMIT 1) 
    AND ingredient_id = (SELECT id FROM ingredient_ids OFFSET 4 LIMIT 1)
)
UNION ALL
SELECT 
    (SELECT id FROM recipe_ids OFFSET 1 LIMIT 1),
    (SELECT id FROM ingredient_ids OFFSET 5 LIMIT 1), -- Panceta
    150
WHERE NOT EXISTS (
    SELECT 1 FROM recipe_ingredients 
    WHERE recipe_id = (SELECT id FROM recipe_ids OFFSET 1 LIMIT 1) 
    AND ingredient_id = (SELECT id FROM ingredient_ids OFFSET 5 LIMIT 1)
)
UNION ALL
SELECT 
    (SELECT id FROM recipe_ids OFFSET 2 LIMIT 1), -- Ensalada César
    (SELECT id FROM ingredient_ids OFFSET 6 LIMIT 1), -- Lechuga
    1
WHERE NOT EXISTS (
    SELECT 1 FROM recipe_ingredients 
    WHERE recipe_id = (SELECT id FROM recipe_ids OFFSET 2 LIMIT 1) 
    AND ingredient_id = (SELECT id FROM ingredient_ids OFFSET 6 LIMIT 1)
)
UNION ALL
SELECT 
    (SELECT id FROM recipe_ids OFFSET 2 LIMIT 1),
    (SELECT id FROM ingredient_ids OFFSET 7 LIMIT 1), -- Crutones
    100
WHERE NOT EXISTS (
    SELECT 1 FROM recipe_ingredients 
    WHERE recipe_id = (SELECT id FROM recipe_ids OFFSET 2 LIMIT 1) 
    AND ingredient_id = (SELECT id FROM ingredient_ids OFFSET 7 LIMIT 1)
)
UNION ALL
SELECT 
    (SELECT id FROM recipe_ids OFFSET 2 LIMIT 1),
    (SELECT id FROM ingredient_ids OFFSET 8 LIMIT 1), -- Aceite de oliva
    50
WHERE NOT EXISTS (
    SELECT 1 FROM recipe_ingredients 
    WHERE recipe_id = (SELECT id FROM recipe_ids OFFSET 2 LIMIT 1) 
    AND ingredient_id = (SELECT id FROM ingredient_ids OFFSET 8 LIMIT 1)
); 