# Configuración de SonarQube

Este directorio contiene la configuración necesaria para ejecutar SonarQube usando Docker Compose.

## Requisitos Previos

- Docker
- Docker Compose
- Al menos 4GB de RAM disponible
- Puertos 9000 disponibles en tu máquina

## Configuración del Sistema

Antes de iniciar SonarQube, necesitas configurar algunos parámetros del sistema. En sistemas Linux, ejecuta:

```bash
sudo sysctl -w vm.max_map_count=262144
sudo sysctl -w fs.file-max=65536
```

## Iniciar SonarQube

Para iniciar SonarQube, ejecuta el siguiente comando en este directorio:

```bash
docker-compose up -d
```

## Acceder a SonarQube

Una vez que los contenedores estén en ejecución, puedes acceder a SonarQube en:

http://localhost:9000

Credenciales por defecto:
- Usuario: admin
- Contraseña: admin

## Detener SonarQube

Para detener los contenedores:

```bash
docker-compose down
```

Para detener y eliminar todos los volúmenes (esto eliminará todos los datos):

```bash
docker-compose down -v
``` 