# Use an official JDK runtime as a parent image
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven wrapper and POM file
COPY ../mvnw ./
COPY ../.mvn .mvn
COPY ../pom.xml ./

# Copy the source code
COPY ../src ./src

# Package the application (skip tests if necessary)
# RUN ./mvnw package && java -jar target/gs-spring-boot-docker-0.1.0.jar -DskipTests
RUN ./mvnw package -DskipTests && java -jar target/gs-spring-boot-docker-0.1.0.jar

# Ensure the JAR file is present
RUN ls -l target/

# Copy the built JAR file to the container
COPY target/gs-spring-boot-docker-0.1.0.jar app.jar

# Expose the application port
EXPOSE 5123

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
