# Use an official OpenJDK runtime as the base image
FROM openjdk:21-jdk-slim

# Copy the Spring Boot application's JAR file into the container
COPY target/ecom-auth-service-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your Spring Boot application runs on
EXPOSE 8080

# Command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]