FROM eclipse-temurin:21-jre-jammy
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
# Importante: El backend suele usar el puerto 8080
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
