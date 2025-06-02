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

echo -e "${BLUE}Iniciando proceso de backup de bases de datos...${NC}"

# Verificar conexiones antes de comenzar
if ! check_database_connection "stock-db-1" "postgres" "5434"; then
    echo -e "${RED}Error: No se pudo conectar a la base de datos de stock${NC}"
    exit 1
fi

if ! check_database_connection "order-db-1" "postgres" "5432"; then
    echo -e "${RED}Error: No se pudo conectar a la base de datos de order${NC}"
    exit 1
fi

# Crear directorio de backup si no existe
mkdir -p database/backups

# Backup de la base de datos de stock
echo -e "${GREEN}Realizando backup de la base de datos de stock...${NC}"
docker exec -i stock-db-1 pg_dump -U postgres -d postgres > database/backups/stock_backup_$(date +%Y%m%d_%H%M%S).sql

# Backup de la base de datos de order
echo -e "${GREEN}Realizando backup de la base de datos de order...${NC}"
docker exec -i order-db-1 pg_dump -U postgres -d postgres > database/backups/order_backup_$(date +%Y%m%d_%H%M%S).sql

echo -e "${BLUE}¡Proceso de backup completado!${NC}"
echo -e "${GREEN}Los archivos de backup se encuentran en el directorio database/backups${NC}" 