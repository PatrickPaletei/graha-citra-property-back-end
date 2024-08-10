# Use an official JDK runtime as a parent image
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven wrapper and POM file
COPY mvnw ./
COPY .mvn .mvn
COPY pom.xml ./

# Copy the source code
COPY src ./src

# Package the application (skip tests if necessary)
RUN ./mvnw package -DskipTests

# Print the contents of the target directory for debugging
RUN ls -la target

# Specify the JAR file to be executed
CMD ["java", "-jar", "target/Citra-Graha-Property-0.0.1-SNAPSHOT.jar"]


