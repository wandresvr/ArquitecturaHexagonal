#!/bin/bash

# Colores para los mensajes
GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Función para verificar la conexión a una base de datos
check_database_connection() {
    local container=$1
    local db=$2
    local port=$3
    
    echo -e "${BLUE}Verificando conexión a ${db} en ${container} (puerto ${port})...${NC}"
    
    # Verificar si el contenedor está corriendo
    if ! docker ps | grep -q $container; then
        echo -e "${RED}Error: El contenedor ${container} no está corriendo${NC}"
        return 1
    fi
    
    # Verificar la conexión a la base de datos
    if ! docker exec -i $container psql -U postgres -d $db -c "SELECT current_database(), current_user, version();" > /dev/null 2>&1; then
        echo -e "${RED}Error: No se puede conectar a la base de datos ${db} en ${container}${NC}"
        return 1
    fi
    
    # Mostrar información de la base de datos
    echo -e "${GREEN}Conexión exitosa a ${db}${NC}"
    docker exec -i $container psql -U postgres -d $db -c "SELECT current_database() as database, current_user as user, version() as version;"
    return 0
}

echo -e "${BLUE}Iniciando proceso de inicialización de bases de datos...${NC}"

# Verificar conexiones antes de comenzar
if ! check_database_connection "stock-db-1" "postgres" "5434"; then
    echo -e "${RED}Error: No se pudo conectar a la base de datos de stock${NC}"
    exit 1
fi

if ! check_database_connection "order-db-1" "postgres" "5432"; then
    echo -e "${RED}Error: No se pudo conectar a la base de datos de order${NC}"
    exit 1
fi

# 1. Ejecutar script de stock
echo -e "${GREEN}Ejecutando script de inicialización de stock...${NC}"
docker exec -i stock-db-1 psql -U postgres -d postgres < database/stock_init.sql

# 2. Exportar IDs de recetas
echo -e "${GREEN}Exportando IDs de recetas...${NC}"
docker exec -i stock-db-1 psql -U postgres -d postgres -c "COPY (SELECT id, name FROM recipes ORDER BY name) TO STDOUT WITH CSV" > recipe_ids.csv

# 3. Copiar archivo CSV al contenedor de order
echo -e "${GREEN}Copiando IDs de recetas al contenedor de order...${NC}"
docker cp recipe_ids.csv order-db-1:/tmp/recipe_ids.csv

# 4. Ejecutar script de order
echo -e "${GREEN}Ejecutando script de inicialización de order...${NC}"
docker exec -i order-db-1 psql -U postgres -d postgres < database/order_init.sql

# 5. Limpiar archivo CSV temporal
echo -e "${GREEN}Limpiando archivos temporales...${NC}"
rm recipe_ids.csv

# 6. Verificar la sincronización
echo -e "${GREEN}Verificando sincronización de IDs...${NC}"
echo -e "${BLUE}IDs de recetas en stock (puerto 5434):${NC}"
docker exec -i stock-db-1 psql -U postgres -d postgres -c "SELECT id, name FROM recipes ORDER BY name;"
echo -e "\n${BLUE}IDs de productos en order (puerto 5432):${NC}"
docker exec -i order-db-1 psql -U postgres -d postgres -c "SELECT id, name, recipe_id FROM products ORDER BY name;"

echo -e "${BLUE}¡Proceso completado!${NC}" 