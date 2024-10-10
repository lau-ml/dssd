# Etapa de construcción
FROM maven:3.9.4-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package

# Etapa de ejecución
FROM openjdk:21-jdk-bullseye
COPY --from=builder /app/target/api-ecocycle-0.0.1-SNAPSHOT.jar java-api-ecocycle.jar
ENTRYPOINT ["java", "-jar", "java-api-ecocycle.jar"]
