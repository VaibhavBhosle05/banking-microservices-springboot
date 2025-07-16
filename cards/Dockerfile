# Step 1: Use a lightweight base image with Java 17
FROM openjdk:17-jdk-slim

# Step 2: Set the working directory inside the container
WORKDIR /app

# Step 3: Copy the generated JAR file into the container
COPY target/Cards-0.0.1-SNAPSHOT.jar app.jar

# Step 4: Expose the application's port (adjust if needed)
EXPOSE 9000

# Step 5: Set the command to run your Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
