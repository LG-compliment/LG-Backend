# Use OpenJDK 17 as the base image, Debian-based slim image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the Gradle wrapper and necessary files first to take advantage of Docker cache
COPY gradlew /app/gradlew
COPY gradle /app/gradle
COPY build.gradle /app/build.gradle
COPY settings.gradle /app/settings.gradle

# Give execute permission to the Gradle wrapper
RUN chmod +x ./gradlew

# Download dependencies
RUN ./gradlew dependencies --no-daemon

# Copy the rest of the application source code
COPY . /app

# Build the application
RUN ./gradlew clean build --no-daemon

# Expose the application port
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "build/libs/compliment-0.0.1-SNAPSHOT.jar"]
