# Abrir puertos en máquina virtual
Se abrirán los puertos necesarios para los microservicios

### Dar permisos al archivo:

```bash
sudo chmod +x open_ports.sh
```

###  Ejecutar:

```bash
sudo ./open_ports.sh
```

# Gestionar contenedores de los microservicios

### Dar permisos al archivo:

```bash
sudo chmod +x start-services.sh
```

###  Iniciar contenedores:

```bash
sudo ./start-services.sh start
```

###  parar contenedores:

```bash
sudo ./start-services.sh stop
```

###  reiniciar contenedores:

```bash
sudo ./start-services.sh restart
```

###  reconstruir contenedores:

```bash
sudo ./start-services.sh build
```