-- Crear tabla de clientes si no existe
CREATE TABLE IF NOT EXISTS clients (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Crear tabla de productos si no existe
CREATE TABLE IF NOT EXISTS products (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10,2),
    recipe_id UUID NOT NULL, -- Referencia a la receta en la base de datos de stock
    stock INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Crear tabla de órdenes si no existe
CREATE TABLE IF NOT EXISTS orders (
    order_id UUID PRIMARY KEY,
    client_id UUID REFERENCES clients(id),
    order_status VARCHAR(50),
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    delivery_street VARCHAR(200),
    delivery_city VARCHAR(100),
    delivery_state VARCHAR(100),
    delivery_zip_code VARCHAR(20),
    delivery_country VARCHAR(100),
    total_amount DECIMAL(10,2),
    total_currency VARCHAR(3) DEFAULT 'USD'
);

-- Crear tabla de items de orden si no existe
CREATE TABLE IF NOT EXISTS order_items (
    id UUID PRIMARY KEY,
    order_id UUID REFERENCES orders(order_id),
    product_id UUID REFERENCES products(id),
    quantity INTEGER NOT NULL
);

-- Insertar datos de ejemplo para clientes (solo si no existen)
INSERT INTO clients (id, name, email, phone)
SELECT gen_random_uuid(), 'Juan Pérez', 'juan@email.com', '1234567890'
WHERE NOT EXISTS (SELECT 1 FROM clients WHERE email = 'juan@email.com');

INSERT INTO clients (id, name, email, phone)
SELECT gen_random_uuid(), 'María García', 'maria@email.com', '0987654321'
WHERE NOT EXISTS (SELECT 1 FROM clients WHERE email = 'maria@email.com');

-- Crear tabla temporal para los IDs de las recetas
CREATE TEMPORARY TABLE temp_recipe_ids (
    id UUID,
    name VARCHAR(100)
);

-- Copiar los IDs de las recetas desde el archivo CSV
\COPY temp_recipe_ids FROM '/tmp/recipe_ids.csv' WITH CSV;

-- Insertar productos usando los IDs de las recetas (solo si no existen)
INSERT INTO products (id, name, description, price, recipe_id, stock)
SELECT 
    r.id,
    CASE 
        WHEN r.name = 'Pizza Margherita' THEN 'Pizza Margherita Familiar'
        WHEN r.name = 'Pasta Carbonara' THEN 'Pasta Carbonara Individual'
        WHEN r.name = 'Ensalada César' THEN 'Ensalada César Premium'
        ELSE 'Producto Genérico' -- Evita valores nulos
    END as name,
    CASE 
        WHEN r.name = 'Pizza Margherita' THEN 'Pizza grande para 4 personas'
        WHEN r.name = 'Pasta Carbonara' THEN 'Porción individual de pasta carbonara'
        WHEN r.name = 'Ensalada César' THEN 'Ensalada César con pollo a la parrilla'
        ELSE 'Descripción genérica' -- Evita valores nulos
    END as description,
    CASE 
        WHEN r.name = 'Pizza Margherita' THEN 25.99
        WHEN r.name = 'Pasta Carbonara' THEN 12.99
        WHEN r.name = 'Ensalada César' THEN 8.99
        ELSE 0.00 -- Evita valores nulos
    END as price,
    r.id as recipe_id,
    CASE 
        WHEN r.name = 'Pizza Margherita' THEN 10
        WHEN r.name = 'Pasta Carbonara' THEN 15
        WHEN r.name = 'Ensalada César' THEN 20
        ELSE 0 -- Evita valores nulos
    END as stock
FROM temp_recipe_ids r
WHERE r.name IN ('Pizza Margherita', 'Pasta Carbonara', 'Ensalada César')
AND NOT EXISTS (SELECT 1 FROM products WHERE recipe_id = r.id);

-- Limpiar la tabla temporal
DROP TABLE temp_recipe_ids; 