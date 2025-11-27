# Stage 1: Build the Spring Boot app using Maven
FROM maven:3.9.2-eclipse-temurin-21 AS build

# Set working directory
WORKDIR /app

# Copy pom.xml first to cache dependencies
COPY pom.xml .

# Download dependencies only
RUN mvn dependency:go-offline -B

# Copy the source code
COPY src ./src

# Build the jar (skip tests to speed up)
RUN mvn clean package -DskipTests

# Stage 2: Run the app
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080 for Railway
EXPOSE 8080

# Command to run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
