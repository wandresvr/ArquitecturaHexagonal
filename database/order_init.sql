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

-- Agregar columnas si no existen
DO $$ 
BEGIN
    -- Agregar columna address si no existe
    IF NOT EXISTS (
        SELECT 1 
        FROM information_schema.columns 
        WHERE table_name = 'clients' 
        AND column_name = 'address'
    ) THEN
        ALTER TABLE clients ADD COLUMN address TEXT;
    END IF;

    -- Agregar columna updated_at si no existe
    IF NOT EXISTS (
        SELECT 1 
        FROM information_schema.columns 
        WHERE table_name = 'clients' 
        AND column_name = 'updated_at'
    ) THEN
        ALTER TABLE clients ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
    END IF;

    -- Agregar restricción UNIQUE a email si no existe
    IF NOT EXISTS (
        SELECT 1 
        FROM information_schema.table_constraints 
        WHERE table_name = 'clients' 
        AND constraint_name = 'clients_email_key'
    ) THEN
        ALTER TABLE clients ADD CONSTRAINT clients_email_key UNIQUE (email);
    END IF;
END $$;

-- Crear tabla de productos si no existe
CREATE TABLE IF NOT EXISTS products (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    stock INTEGER NOT NULL DEFAULT 0,
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

-- Insertar datos de ejemplo de clientes
INSERT INTO clients (id, name, email, phone, address)
VALUES 
    (gen_random_uuid(), 'Juan Pérez', 'juan@example.com', '1234567890', 'Calle Principal 123')
ON CONFLICT (email) DO NOTHING;

INSERT INTO clients (id, name, email, phone, address)
VALUES 
    (gen_random_uuid(), 'María García', 'maria@example.com', '0987654321', 'Avenida Central 456')
ON CONFLICT (email) DO NOTHING;

-- Limpiar productos existentes
TRUNCATE TABLE products CASCADE;

-- Crear tabla temporal para los IDs de recetas
CREATE TEMPORARY TABLE temp_recipe_ids (
    id UUID,
    name VARCHAR(255)
);

-- Copiar IDs de recetas desde el archivo CSV
COPY temp_recipe_ids FROM '/tmp/recipe_ids.csv' WITH CSV;

-- Insertar productos basados en las recetas
INSERT INTO products (id, name, description, price, stock)
SELECT 
    r.id,
    CASE 
        WHEN r.name = 'Ensalada César' THEN 'Ensalada César Premium'
        WHEN r.name = 'Pasta Carbonara' THEN 'Pasta Carbonara Individual'
        WHEN r.name = 'Pizza Margherita' THEN 'Pizza Margherita Familiar'
    END,
    'Producto basado en ' || r.name,
    CASE 
        WHEN r.name = 'Ensalada César' THEN 12.99
        WHEN r.name = 'Pasta Carbonara' THEN 15.99
        WHEN r.name = 'Pizza Margherita' THEN 14.99
        ELSE 10.99
    END,
    100
FROM temp_recipe_ids r;

-- Eliminar tabla temporal
DROP TABLE temp_recipe_ids; 