# Usar una imagen base de Gradle con Java 17
FROM gradle:8.5.0-jdk17 AS build

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar los archivos de construcción
COPY . .

# Construir la aplicación
RUN gradle build --no-daemon

# Usar una imagen base más ligera para la ejecución
FROM openjdk:17-jdk-slim

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el JAR desde la etapa de construcción
COPY --from=build /app/build/libs/*.jar app.jar

# Exponer el puerto que usa la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]