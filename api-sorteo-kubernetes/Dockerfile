# Etapa de construcción
FROM maven:3.9.4-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-bullseye

# Copia el archivo .jar desde la carpeta target al contenedor
COPY --from=builder /app/target/api-sorteo-kubernetes-0.0.1-SNAPSHOT.jar java-app.jar

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "java-app.jar"]
