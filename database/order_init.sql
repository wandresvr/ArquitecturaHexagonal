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
    recipe_id UUID NOT NULL UNIQUE,
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

-- Insertar productos basados en las recetas del archivo CSV
DO $$
DECLARE
    recipe_record RECORD;
BEGIN
    FOR recipe_record IN 
        SELECT id, name 
        FROM pg_read_csv('/tmp/recipe_ids.csv', 'id UUID, name VARCHAR(255)')
    LOOP
        INSERT INTO products (id, name, description, price, recipe_id)
        VALUES (
            gen_random_uuid(),
            CASE 
                WHEN recipe_record.name = 'Ensalada César' THEN 'Ensalada César Premium'
                WHEN recipe_record.name = 'Pasta Carbonara' THEN 'Pasta Carbonara Individual'
                WHEN recipe_record.name = 'Pizza Margherita' THEN 'Pizza Margherita Familiar'
            END,
            'Producto basado en ' || recipe_record.name,
            CASE 
                WHEN recipe_record.name = 'Ensalada César' THEN 12.99
                WHEN recipe_record.name = 'Pasta Carbonara' THEN 15.99
                WHEN recipe_record.name = 'Pizza Margherita' THEN 14.99
                ELSE 10.99
            END,
            recipe_record.id
        );
    END LOOP;
END $$; 