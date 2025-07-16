# Use a minimal Java 17 image
FROM openjdk:17-jdk-slim

# Set metadata (optional)
#LABEL maintainer="vaibhav@example.com"

# Set working directory inside the container
WORKDIR /app

# Copy the JAR from your local system to container's /app
COPY target/loans-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8090 (from your application.yml)
EXPOSE 8090

# Start the app
ENTRYPOINT ["java", "-jar", "app.jar"]
