# Dockerfile for using jar package
FROM openjdk:21
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

# Dockerfile.2 for using maven 
# FROM maven:3.9.6-eclipse-temurin-21

# WORKDIR /vollmed_backend
# COPY . .

# CMD ["mvn", "spring-boot:run"]
