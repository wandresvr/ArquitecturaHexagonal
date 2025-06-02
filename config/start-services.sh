#!/bin/bash

# Obtener el directorio del script
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

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

# Función para obtener la IP pública
get_public_ip() {
    local ip=$(curl -s ifconfig.me)
    if [ -z "$ip" ]; then
        print_error "No se pudo obtener la IP pública"
        exit 1
    fi
    echo "$ip"
}

# Función para verificar si Docker está corriendo
check_docker() {
    if ! docker info > /dev/null 2>&1; then
        print_error "Docker no está corriendo. Por favor, inicia Docker primero."
        exit 1
    fi
}

# Función para configurar variables de entorno permanentemente
setup_environment() {
    local ip="$1"
    
    # Crear archivo en /etc/profile.d/
    local env_file="/etc/profile.d/swagger-server.sh"
    echo "export SWAGGER_SERVER_URL=http://$ip" | sudo tee "$env_file" > /dev/null
    sudo chmod +x "$env_file" > /dev/null
    
    # Agregar también a /etc/environment
    if ! grep -q "SWAGGER_SERVER_URL" /etc/environment; then
        echo "SWAGGER_SERVER_URL=http://$ip" | sudo tee -a /etc/environment > /dev/null
    else
        sudo sed -i "s|SWAGGER_SERVER_URL=.*|SWAGGER_SERVER_URL=http://$ip|" /etc/environment > /dev/null
    fi
    
    # Exportar la variable para la sesión actual
    export SWAGGER_SERVER_URL="http://$ip"
    
    # Cargar la variable en la sesión actual
    source /etc/environment > /dev/null 2>&1
    
    print_message "Variable de sistema SWAGGER_SERVER_URL configurada permanentemente como http://$ip"
}

# Función para iniciar los servicios
start_services() {
    print_message "Iniciando servicios..."
    
    # Obtener IP pública
    PUBLIC_IP=$(get_public_ip)
    print_message "IP pública detectada: $PUBLIC_IP"
    
    # Configurar variables de entorno
    setup_environment "$PUBLIC_IP"
    
    # Iniciar RabbitMQ
    print_message "Iniciando RabbitMQ..."
    cd "$PROJECT_ROOT/rabbitmq" && docker-compose up -d
    if [ $? -ne 0 ]; then
        print_error "Error al iniciar RabbitMQ"
        exit 1
    fi
    
    # Iniciar Order Service
    print_message "Iniciando Order Service..."
    cd "$PROJECT_ROOT/order" && docker-compose up -d
    if [ $? -ne 0 ]; then
        print_error "Error al iniciar Order Service"
        exit 1
    fi
    
    # Iniciar Stock Service
    print_message "Iniciando Stock Service..."
    cd "$PROJECT_ROOT/stock" && docker-compose up -d
    if [ $? -ne 0 ]; then
        print_error "Error al iniciar Stock Service"
        exit 1
    fi
    
    cd "$SCRIPT_DIR"
    print_success "Servicios iniciados correctamente"
    print_message "RabbitMQ Management UI: http://$PUBLIC_IP:15672"
    print_message "Order Service: http://$PUBLIC_IP:8080"
    print_message "Stock Service: http://$PUBLIC_IP:8081"
}

# Función para detener los servicios
stop_services() {
    print_message "Deteniendo servicios..."
    
    # Detener Stock Service
    print_message "Deteniendo Stock Service..."
    cd "$PROJECT_ROOT/stock" && docker-compose down
    
    # Detener Order Service
    print_message "Deteniendo Order Service..."
    cd "$PROJECT_ROOT/order" && docker-compose down
    
    # Detener RabbitMQ
    print_message "Deteniendo RabbitMQ..."
    cd "$PROJECT_ROOT/rabbitmq" && docker-compose down
    
    cd "$SCRIPT_DIR"
    print_success "Servicios detenidos correctamente"
}

# Función para mostrar el estado de los servicios
status_services() {
    print_message "Estado de los servicios:"
    echo "RabbitMQ:"
    cd "$PROJECT_ROOT/rabbitmq" && docker-compose ps
    echo "Order Service:"
    cd "$PROJECT_ROOT/order" && docker-compose ps
    echo "Stock Service:"
    cd "$PROJECT_ROOT/stock" && docker-compose ps
    cd "$SCRIPT_DIR"
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
            cd "$PROJECT_ROOT/rabbitmq" && docker-compose logs -f
            ;;
        2)
            cd "$PROJECT_ROOT/order" && docker-compose logs -f
            ;;
        3)
            cd "$PROJECT_ROOT/stock" && docker-compose logs -f
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