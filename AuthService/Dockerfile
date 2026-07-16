# Use Amazon Corretto 21 with platform specification
# Use an official OpenJDK 17 base image instead of 21
FROM --platform=linux/amd64 openjdk:17-jdk-slim

WORKDIR /app

# Copy the JAR file from your local build directory to the container
COPY build/libs/AuthService-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose the port matching your docker-compose mappings
EXPOSE 9898

# Set the entry point for the container
ENTRYPOINT ["java", "-jar", "/app/app.jar"]