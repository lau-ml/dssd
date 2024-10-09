FROM openjdk:21-jdk-bullseye

# Copia el archivo .jar desde la carpeta target al contenedor
COPY ./target/api-ecocycle-0.0.1-SNAPSHOT.jar java-api-ecocycle.jar

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "java-api-ecocycle.jar"]
