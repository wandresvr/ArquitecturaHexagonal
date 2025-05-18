FROM gradle:8.5.0-jdk17 AS build
WORKDIR /workspace/app

COPY . .
RUN ./gradlew build -x test

FROM openjdk:17-jdk-slim
COPY --from=build /workspace/app/build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]