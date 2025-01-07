# Build stage
FROM maven:3.8.6-eclipse-temurin-17 AS build
# Copy source code and pom.xml file to /app folder
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build source code with maven
RUN mvn package -DskipTests

# Runtime stage
FROM amazoncorretto:17
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]