# Use a base image with Java 17
FROM openjdk:17

# Copy the JAR package into the image
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Expose the application port
EXPOSE 8090

# Set JVM options to limit memory usage to 256MB
ENV JAVA_OPTS="-Xmx256m"

# Run the App
ENTRYPOINT ["java", "-jar", "/app.jar"]
