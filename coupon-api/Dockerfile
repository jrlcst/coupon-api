# Stage 1: tests/build (has mvn)
FROM maven:3-eclipse-temurin-25 AS build
WORKDIR /app

COPY pom.xml .
COPY .mvn .mvn
COPY mvnw mvnw
COPY src src

# roda testes
RUN ./mvnw -q test

# empacota app
RUN ./mvnw -q package -DskipTests

# Stage 2: runtime (small)
FROM eclipse-temurin:25-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]