-- Crear tabla de clientes si no existe
CREATE TABLE IF NOT EXISTS clients (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(20),
    address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Crear tabla de productos si no existe
CREATE TABLE IF NOT EXISTS products (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    recipe_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Crear tabla de órdenes si no existe
CREATE TABLE IF NOT EXISTS orders (
    id UUID PRIMARY KEY,
    client_id UUID REFERENCES clients(id),
    status VARCHAR(50) NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Crear tabla de items de orden si no existe
CREATE TABLE IF NOT EXISTS order_items (
    id UUID PRIMARY KEY,
    order_id UUID REFERENCES orders(id),
    product_id UUID REFERENCES products(id),
    quantity INTEGER NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Crear tabla temporal para los IDs de recetas
CREATE TEMPORARY TABLE temp_recipe_ids (
    id UUID,
    name VARCHAR(255)
);

-- Copiar IDs de recetas desde el archivo CSV
COPY temp_recipe_ids(id, name) FROM '/tmp/recipe_ids.csv' WITH CSV;

-- Insertar datos de ejemplo para clientes
INSERT INTO clients (id, name, email, phone, address)
VALUES 
    (gen_random_uuid(), 'Juan Pérez', 'juan@email.com', '1234567890', 'Calle Principal 123')
ON CONFLICT (email) DO NOTHING;

INSERT INTO clients (id, name, email, phone, address)
VALUES 
    (gen_random_uuid(), 'María García', 'maria@email.com', '0987654321', 'Avenida Central 456')
ON CONFLICT (email) DO NOTHING;

-- Insertar productos basados en las recetas
INSERT INTO products (id, name, description, price, recipe_id)
SELECT 
    gen_random_uuid(),
    CASE 
        WHEN name = 'Ensalada César' THEN 'Ensalada César Premium'
        WHEN name = 'Pasta Carbonara' THEN 'Pasta Carbonara Individual'
        WHEN name = 'Pizza Margherita' THEN 'Pizza Margherita Familiar'
    END,
    CASE 
        WHEN name = 'Ensalada César' THEN 'Ensalada César con pollo a la parrilla y aderezo especial'
        WHEN name = 'Pasta Carbonara' THEN 'Pasta Carbonara con panceta y queso pecorino'
        WHEN name = 'Pizza Margherita' THEN 'Pizza Margherita con tomate y mozzarella fresca'
    END,
    CASE 
        WHEN name = 'Ensalada César' THEN 15.99
        WHEN name = 'Pasta Carbonara' THEN 12.99
        WHEN name = 'Pizza Margherita' THEN 18.99
    END,
    id
FROM temp_recipe_ids
ON CONFLICT (id) DO NOTHING;

-- Eliminar tabla temporal
DROP TABLE temp_recipe_ids; 