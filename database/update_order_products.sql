-- Primero, crear una tabla temporal para almacenar los IDs de las recetas
CREATE TEMPORARY TABLE temp_recipe_ids AS
SELECT id, name
FROM recipes
ORDER BY name;

-- Actualizar los productos con los IDs de las recetas correspondientes
UPDATE products p
SET id = r.id
FROM temp_recipe_ids r
WHERE 
    CASE 
        WHEN p.name = 'Pizza Margherita Familiar' THEN r.name = 'Pizza Margherita'
        WHEN p.name = 'Pasta Carbonara Individual' THEN r.name = 'Pasta Carbonara'
        WHEN p.name = 'Ensalada César Premium' THEN r.name = 'Ensalada César'
    END;

-- Limpiar la tabla temporal
DROP TABLE temp_recipe_ids; 