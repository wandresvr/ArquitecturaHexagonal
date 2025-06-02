#!/bin/bash

# Colores para mensajes
GREEN='\033[0;32m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Función para mostrar mensajes
print_message() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Función para verificar si Docker está corriendo
check_docker() {
    if ! docker info > /dev/null 2>&1; then
        print_error "Docker no está corriendo. Por favor, inicia Docker primero."
        exit 1
    fi
}

# Función para iniciar los servicios
start_services() {
    print_message "Iniciando servicios..."
    
    # Iniciar RabbitMQ
    print_message "Iniciando RabbitMQ..."
    cd ../rabbitmq && docker-compose up -d
    if [ $? -ne 0 ]; then
        print_error "Error al iniciar RabbitMQ"
        exit 1
    fi
    
    # Iniciar Order Service
    print_message "Iniciando Order Service..."
    cd ../order && docker-compose up -d
    if [ $? -ne 0 ]; then
        print_error "Error al iniciar Order Service"
        exit 1
    fi
    
    # Iniciar Stock Service
    print_message "Iniciando Stock Service..."
    cd ../stock && docker-compose up -d
    if [ $? -ne 0 ]; then
        print_error "Error al iniciar Stock Service"
        exit 1
    fi
    
    cd ../config
    print_success "Servicios iniciados correctamente"
    print_message "RabbitMQ Management UI: http://localhost:15672"
    print_message "Order Service: http://localhost:8080"
    print_message "Stock Service: http://localhost:8081"
}

# Función para detener los servicios
stop_services() {
    print_message "Deteniendo servicios..."
    
    # Detener Stock Service
    print_message "Deteniendo Stock Service..."
    cd ../stock && docker-compose down
    
    # Detener Order Service
    print_message "Deteniendo Order Service..."
    cd ../order && docker-compose down
    
    # Detener RabbitMQ
    print_message "Deteniendo RabbitMQ..."
    cd ../rabbitmq && docker-compose down
    
    cd ../config
    print_success "Servicios detenidos correctamente"
}

# Función para mostrar el estado de los servicios
status_services() {
    print_message "Estado de los servicios:"
    echo "RabbitMQ:"
    cd ../rabbitmq && docker-compose ps
    echo "Order Service:"
    cd ../order && docker-compose ps
    echo "Stock Service:"
    cd ../stock && docker-compose ps
    cd ../config
}

# Función para mostrar los logs
show_logs() {
    print_message "Mostrando logs de los servicios..."
    echo "Seleccione el servicio para ver sus logs:"
    echo "1) RabbitMQ"
    echo "2) Order Service"
    echo "3) Stock Service"
    read -p "Ingrese el número del servicio: " choice
    
    case $choice in
        1)
            cd ../rabbitmq && docker-compose logs -f
            ;;
        2)
            cd ../order && docker-compose logs -f
            ;;
        3)
            cd ../stock && docker-compose logs -f
            ;;
        *)
            print_error "Opción inválida"
            exit 1
            ;;
    esac
}

# Menú principal
case "$1" in
    "start")
        check_docker
        start_services
        ;;
    "stop")
        stop_services
        ;;
    "restart")
        stop_services
        start_services
        ;;
    "status")
        status_services
        ;;
    "logs")
        show_logs
        ;;
    *)
        echo "Uso: $0 {start|stop|restart|status|logs}"
        exit 1
        ;;
esac

exit 0 