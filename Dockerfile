# Use lightweight Java runtime
FROM eclipse-temurin:17-jdk-jammy

# Set working directory
WORKDIR /app

# Copy the built jar from Maven target folder
COPY target/devops-demo-app-1.0.jar app.jar

# Expose application port
EXPOSE 9090

# Run the application
ENTRYPOINT ["java","-jar","/app/app.jar"]
