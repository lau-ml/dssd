FROM openjdk:21-jdk-bullseye

# Copia el archivo .jar desde la carpeta target al contenedor
COPY ./target/server-0.0.1-SNAPSHOT.jar java-app.jar

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "java-app.jar"]
