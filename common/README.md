# ArquitecuraHexagonal

Librería común para microservicios con arquitectura hexagonal.

## Instalación

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.wandresvr:ArquitecuraHexagonal:1.0.0'
}
```

## Uso

La librería proporciona DTOs comunes para la comunicación entre microservicios:

- `OrderMessageDTO`: Mensaje para la creación de órdenes
- `ClientDTO`: Información del cliente
- `AddressShippingDTO`: Dirección de envío
- `ProductOrderDTO`: Producto en una orden

## Estructura del Proyecto

```
src/main/java/com/itm/edu/common/dto/
├── OrderMessageDTO.java
├── ClientDTO.java
├── AddressShippingDTO.java
└── ProductOrderDTO.java
```

## Licencia

Apache License 2.0 