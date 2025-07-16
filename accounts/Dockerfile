# Use a base image with Java
FROM openjdk:17-jdk-slim

# Metadata
LABEL maintainer="anonymous.com"

# Copy JAR from target directory to image root
COPY target/accounts-0.0.1-SNAPSHOT.jar accounts-0.0.1-SNAPSHOT.jar

# Command to run the JAR
ENTRYPOINT ["java", "-jar", "accounts-0.0.1-SNAPSHOT.jar"]
