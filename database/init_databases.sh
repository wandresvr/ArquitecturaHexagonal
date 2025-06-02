#!/bin/bash

# Colores para los mensajes
GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Verificar que estamos en el directorio correcto
if [ ! -f "../docker-compose.yml" ]; then
    echo -e "${RED}Error: No se encuentra el archivo docker-compose.yml${NC}"
    echo -e "${RED}Por favor, ejecuta este script desde el directorio database/${NC}"
    exit 1
fi

# Función para limpiar volúmenes
clean_volumes() {
    echo -e "${BLUE}Limpiando volúmenes de Docker...${NC}"
    
    # Detener contenedores si están corriendo
    if docker ps -q | grep -q .; then
        echo -e "${BLUE}Deteniendo contenedores...${NC}"
        cd .. && docker-compose down && cd database
    fi
    
    # Listar y eliminar volúmenes específicos
    for volume in stock-db-data order-db-data; do
        if docker volume ls -q | grep -q "^${volume}$"; then
            echo -e "${BLUE}Eliminando volumen ${volume}...${NC}"
            docker volume rm ${volume}
        fi
    done
    
    echo -e "${GREEN}Limpieza de volúmenes completada${NC}"
}

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

# Función para verificar si existen datos en una tabla
check_table_data() {
    local container=$1
    local db=$2
    local table=$3
    
    echo -e "${BLUE}Verificando datos en la tabla ${table}...${NC}"
    
    local count=$(docker exec -i $container psql -U postgres -d $db -t -c "SELECT COUNT(*) FROM ${table};" | tr -d ' ')
    
    if [ "$count" -eq "0" ]; then
        echo -e "${RED}Error: No hay datos en la tabla ${table}${NC}"
        return 1
    fi
    
    echo -e "${GREEN}Se encontraron ${count} registros en ${table}${NC}"
    return 0
}

echo -e "${BLUE}Iniciando proceso de inicialización de bases de datos...${NC}"

# Limpiar volúmenes existentes
clean_volumes

# Iniciar contenedores
echo -e "${BLUE}Iniciando contenedores...${NC}"
cd .. && docker-compose up -d && cd database

# Esperar a que los contenedores estén listos
echo -e "${BLUE}Esperando a que los contenedores estén listos...${NC}"
sleep 10

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
docker exec -i stock-db-1 psql -U postgres -d postgres < stock_init.sql

# Verificar que se crearon las recetas
if ! check_table_data "stock-db-1" "postgres" "recipes"; then
    echo -e "${RED}Error: No se pudieron crear las recetas en stock${NC}"
    exit 1
fi

# 2. Exportar IDs de recetas
echo -e "${GREEN}Exportando IDs de recetas...${NC}"
docker exec -i stock-db-1 psql -U postgres -d postgres -c "COPY (SELECT id, name FROM recipes ORDER BY name) TO STDOUT WITH CSV" > recipe_ids.csv

# Verificar que el archivo CSV se creó correctamente
if [ ! -s recipe_ids.csv ]; then
    echo -e "${RED}Error: El archivo recipe_ids.csv está vacío${NC}"
    exit 1
fi

# 3. Copiar archivo CSV al contenedor de order
echo -e "${GREEN}Copiando IDs de recetas al contenedor de order...${NC}"
docker cp recipe_ids.csv order-db-1:/tmp/recipe_ids.csv

# Verificar que el archivo se copió correctamente
if ! docker exec -i order-db-1 test -s /tmp/recipe_ids.csv; then
    echo -e "${RED}Error: El archivo recipe_ids.csv no se copió correctamente al contenedor de order${NC}"
    exit 1
fi

# 4. Ejecutar script de order
echo -e "${GREEN}Ejecutando script de inicialización de order...${NC}"
docker exec -i order-db-1 psql -U postgres -d postgres < order_init.sql

# Verificar que se crearon los productos
if ! check_table_data "order-db-1" "postgres" "products"; then
    echo -e "${RED}Error: No se pudieron crear los productos en order${NC}"
    exit 1
fi

# 5. Limpiar archivo CSV temporal
echo -e "${GREEN}Limpiando archivos temporales...${NC}"
rm recipe_ids.csv

# 6. Verificar la sincronización
echo -e "${GREEN}Verificando sincronización de IDs...${NC}"
echo -e "${BLUE}IDs de recetas en stock (puerto 5434):${NC}"
docker exec -i stock-db-1 psql -U postgres -d postgres -c "SELECT id, name FROM recipes ORDER BY name;"
echo -e "\n${BLUE}IDs de productos en order (puerto 5432):${NC}"
docker exec -i order-db-1 psql -U postgres -d postgres -c "SELECT id, name, recipe_id FROM products ORDER BY name;"

echo -e "${GREEN}¡Proceso completado!${NC}" 