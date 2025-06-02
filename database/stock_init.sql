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
        CASE 
            WHEN i.name = 'Lechuga Romana' THEN 200
            WHEN i.name = 'Pollo a la parrilla' THEN 150
            WHEN i.name = 'Crutones' THEN 50
            WHEN i.name = 'Queso Parmesano' THEN 30
            WHEN i.name = 'Aderezo César' THEN 30
            WHEN i.name = 'Espaguetis' THEN 500
            WHEN i.name = 'Panceta' THEN 200
            WHEN i.name = 'Huevo' THEN 2
            WHEN i.name = 'Queso Pecorino' THEN 100
            WHEN i.name = 'Masa de pizza' THEN 300
            WHEN i.name = 'Salsa de tomate' THEN 150
            WHEN i.name = 'Mozzarella' THEN 200
        END as quantity,
        CASE 
            WHEN i.name IN ('Aderezo César', 'Salsa de tomate') THEN 'ml'
            WHEN i.name = 'Huevo' THEN 'unidad'
            ELSE 'g'
        END as unit,
        i.id as ingredient_id,
        r.id as recipe_id
    FROM recipe_ids r
    CROSS JOIN ingredient_ids i
    WHERE (r.name = 'Ensalada César' AND i.name IN ('Lechuga Romana', 'Pollo a la parrilla', 'Crutones', 'Queso Parmesano', 'Aderezo César'))
    OR (r.name = 'Pasta Carbonara' AND i.name IN ('Espaguetis', 'Panceta', 'Huevo', 'Queso Pecorino'))
    OR (r.name = 'Pizza Margherita' AND i.name IN ('Masa de pizza', 'Salsa de tomate', 'Mozzarella'));

    -- Exportar IDs de recetas a CSV
    COPY (SELECT id, name FROM recipes ORDER BY name) TO '/tmp/recipe_ids.csv' WITH CSV;
END $$; 