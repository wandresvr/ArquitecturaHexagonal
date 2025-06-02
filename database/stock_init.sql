-- Crear tabla de recetas si no existe
CREATE TABLE IF NOT EXISTS recipes (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    preparation_time INTEGER,
    cooking_time INTEGER,
    servings INTEGER,
    instructions TEXT,
    difficulty VARCHAR(20) NOT NULL DEFAULT 'MEDIA',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Agregar columnas si no existen
DO $$ 
BEGIN
    -- Agregar columna cooking_time si no existe
    IF NOT EXISTS (
        SELECT 1 
        FROM information_schema.columns 
        WHERE table_name = 'recipes' 
        AND column_name = 'cooking_time'
    ) THEN
        ALTER TABLE recipes ADD COLUMN cooking_time INTEGER;
    END IF;

    -- Agregar columna servings si no existe
    IF NOT EXISTS (
        SELECT 1 
        FROM information_schema.columns 
        WHERE table_name = 'recipes' 
        AND column_name = 'servings'
    ) THEN
        ALTER TABLE recipes ADD COLUMN servings INTEGER;
    END IF;

    -- Agregar columna instructions si no existe
    IF NOT EXISTS (
        SELECT 1 
        FROM information_schema.columns 
        WHERE table_name = 'recipes' 
        AND column_name = 'instructions'
    ) THEN
        ALTER TABLE recipes ADD COLUMN instructions TEXT;
    END IF;

    -- Agregar columna difficulty si no existe
    IF NOT EXISTS (
        SELECT 1 
        FROM information_schema.columns 
        WHERE table_name = 'recipes' 
        AND column_name = 'difficulty'
    ) THEN
        ALTER TABLE recipes ADD COLUMN difficulty VARCHAR(20) NOT NULL DEFAULT 'MEDIA';
    END IF;

    -- Agregar columna updated_at si no existe
    IF NOT EXISTS (
        SELECT 1 
        FROM information_schema.columns 
        WHERE table_name = 'recipes' 
        AND column_name = 'updated_at'
    ) THEN
        ALTER TABLE recipes ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
    END IF;

    -- Agregar columna created_at si no existe
    IF NOT EXISTS (
        SELECT 1 
        FROM information_schema.columns 
        WHERE table_name = 'recipes' 
        AND column_name = 'created_at'
    ) THEN
        ALTER TABLE recipes ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
    END IF;
END $$;

-- Limpiar datos existentes
TRUNCATE TABLE recipes CASCADE;

-- Crear tabla de ingredientes si no existe
CREATE TABLE IF NOT EXISTS ingredients (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    unit VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Crear tabla de relación receta-ingrediente si no existe
CREATE TABLE IF NOT EXISTS recipe_ingredients (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    quantity DECIMAL(10,2) NOT NULL,
    unit VARCHAR(50),
    ingredient_id UUID REFERENCES ingredients(id),
    recipe_id UUID REFERENCES recipes(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insertar datos de ejemplo
DO $$
DECLARE
    v_recipe_id UUID;
    v_ingredient_id UUID;
BEGIN
    -- Limpiar datos existentes
    TRUNCATE TABLE recipes CASCADE;
    TRUNCATE TABLE ingredients CASCADE;
    TRUNCATE TABLE recipe_ingredients CASCADE;

    -- Insertar recetas
    INSERT INTO recipes (id, name, description, preparation_time, cooking_time, servings, instructions, difficulty)
    VALUES 
        (gen_random_uuid(), 'Ensalada César', 'Ensalada clásica con pollo a la parrilla', 15, 20, 2, 'Mezclar todos los ingredientes y servir', 'FÁCIL');

    INSERT INTO recipes (id, name, description, preparation_time, cooking_time, servings, instructions, difficulty)
    VALUES 
        (gen_random_uuid(), 'Pasta Carbonara', 'Pasta con salsa cremosa y panceta', 10, 20, 4, 'Cocer la pasta y mezclar con la salsa', 'MEDIA');

    INSERT INTO recipes (id, name, description, preparation_time, cooking_time, servings, instructions, difficulty)
    VALUES 
        (gen_random_uuid(), 'Pizza Margherita', 'Pizza clásica con tomate y mozzarella', 20, 15, 4, 'Estirar la masa y hornear', 'MEDIA');

    -- Insertar ingredientes
    INSERT INTO ingredients (id, name, description, unit)
    VALUES 
        (gen_random_uuid(), 'Lechuga Romana', 'Lechuga fresca', 'g');

    INSERT INTO ingredients (id, name, description, unit)
    VALUES 
        (gen_random_uuid(), 'Pollo a la parrilla', 'Pechuga de pollo asada', 'g');

    INSERT INTO ingredients (id, name, description, unit)
    VALUES 
        (gen_random_uuid(), 'Crutones', 'Pan tostado en cubos', 'g');

    INSERT INTO ingredients (id, name, description, unit)
    VALUES 
        (gen_random_uuid(), 'Queso Parmesano', 'Queso rallado', 'g');

    INSERT INTO ingredients (id, name, description, unit)
    VALUES 
        (gen_random_uuid(), 'Aderezo César', 'Salsa cremosa', 'ml');

    INSERT INTO ingredients (id, name, description, unit)
    VALUES 
        (gen_random_uuid(), 'Espaguetis', 'Pasta larga', 'g');

    INSERT INTO ingredients (id, name, description, unit)
    VALUES 
        (gen_random_uuid(), 'Panceta', 'Tocino italiano', 'g');

    INSERT INTO ingredients (id, name, description, unit)
    VALUES 
        (gen_random_uuid(), 'Huevo', 'Huevo fresco', 'unidad');

    INSERT INTO ingredients (id, name, description, unit)
    VALUES 
        (gen_random_uuid(), 'Queso Pecorino', 'Queso italiano', 'g');

    INSERT INTO ingredients (id, name, description, unit)
    VALUES 
        (gen_random_uuid(), 'Masa de pizza', 'Masa fresca', 'g');

    INSERT INTO ingredients (id, name, description, unit)
    VALUES 
        (gen_random_uuid(), 'Salsa de tomate', 'Salsa para pizza', 'ml');

    INSERT INTO ingredients (id, name, description, unit)
    VALUES 
        (gen_random_uuid(), 'Mozzarella', 'Queso mozzarella', 'g');

    -- Insertar relaciones receta-ingrediente
    WITH recipe_ids AS (
        SELECT id, name FROM recipes ORDER BY name
    ),
    ingredient_ids AS (
        SELECT id, name FROM ingredients ORDER BY name
    )
    INSERT INTO recipe_ingredients (id, quantity, unit, ingredient_id, recipe_id)
    SELECT 
        gen_random_uuid(),
        quantity,
        unit,
        ingredient_id,
        recipe_id
    FROM (
        -- Ensalada César
        SELECT 
            200 as quantity,
            'g' as unit,
            (SELECT id FROM ingredient_ids WHERE name = 'Lechuga Romana') as ingredient_id,
            (SELECT id FROM recipe_ids WHERE name = 'Ensalada César') as recipe_id
        UNION ALL
        SELECT 
            150 as quantity,
            'g' as unit,
            (SELECT id FROM ingredient_ids WHERE name = 'Pollo a la parrilla') as ingredient_id,
            (SELECT id FROM recipe_ids WHERE name = 'Ensalada César') as recipe_id
        UNION ALL
        SELECT 
            50 as quantity,
            'g' as unit,
            (SELECT id FROM ingredient_ids WHERE name = 'Crutones') as ingredient_id,
            (SELECT id FROM recipe_ids WHERE name = 'Ensalada César') as recipe_id
        UNION ALL
        SELECT 
            30 as quantity,
            'g' as unit,
            (SELECT id FROM ingredient_ids WHERE name = 'Queso Parmesano') as ingredient_id,
            (SELECT id FROM recipe_ids WHERE name = 'Ensalada César') as recipe_id
        UNION ALL
        SELECT 
            30 as quantity,
            'ml' as unit,
            (SELECT id FROM ingredient_ids WHERE name = 'Aderezo César') as ingredient_id,
            (SELECT id FROM recipe_ids WHERE name = 'Ensalada César') as recipe_id
        UNION ALL
        -- Pasta Carbonara
        SELECT 
            500 as quantity,
            'g' as unit,
            (SELECT id FROM ingredient_ids WHERE name = 'Espaguetis') as ingredient_id,
            (SELECT id FROM recipe_ids WHERE name = 'Pasta Carbonara') as recipe_id
        UNION ALL
        SELECT 
            200 as quantity,
            'g' as unit,
            (SELECT id FROM ingredient_ids WHERE name = 'Panceta') as ingredient_id,
            (SELECT id FROM recipe_ids WHERE name = 'Pasta Carbonara') as recipe_id
        UNION ALL
        SELECT 
            2 as quantity,
            'unidad' as unit,
            (SELECT id FROM ingredient_ids WHERE name = 'Huevo') as ingredient_id,
            (SELECT id FROM recipe_ids WHERE name = 'Pasta Carbonara') as recipe_id
        UNION ALL
        SELECT 
            100 as quantity,
            'g' as unit,
            (SELECT id FROM ingredient_ids WHERE name = 'Queso Pecorino') as ingredient_id,
            (SELECT id FROM recipe_ids WHERE name = 'Pasta Carbonara') as recipe_id
        UNION ALL
        -- Pizza Margherita
        SELECT 
            300 as quantity,
            'g' as unit,
            (SELECT id FROM ingredient_ids WHERE name = 'Masa de pizza') as ingredient_id,
            (SELECT id FROM recipe_ids WHERE name = 'Pizza Margherita') as recipe_id
        UNION ALL
        SELECT 
            150 as quantity,
            'ml' as unit,
            (SELECT id FROM ingredient_ids WHERE name = 'Salsa de tomate') as ingredient_id,
            (SELECT id FROM recipe_ids WHERE name = 'Pizza Margherita') as recipe_id
        UNION ALL
        SELECT 
            200 as quantity,
            'g' as unit,
            (SELECT id FROM ingredient_ids WHERE name = 'Mozzarella') as ingredient_id,
            (SELECT id FROM recipe_ids WHERE name = 'Pizza Margherita') as recipe_id
    ) as recipe_ingredients_data;

    -- Exportar IDs de recetas a CSV
    COPY (SELECT id, name FROM recipes ORDER BY name) TO '/tmp/recipe_ids.csv' WITH CSV;
END $$; 