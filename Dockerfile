# the base image
FROM openjdk:17-jdk-alpine
# Set the working directory inside the container
WORKDIR /app

# Copy the Maven wrapper and POM file
COPY ./mvnw ./
COPY ./.mvn .mvn
COPY ./pom.xml ./

# Copy the source code
COPY ./src ./src

RUN ./mvnw package -DskipTests && java -jar target/gs-spring-boot-docker-0.1.0.jar

# the JAR file path
ARG JAR_FILE=target/*.jar

# Copy the JAR file from the build context into the Docker image
COPY ${JAR_FILE} application.jar

CMD apt-get update -y

# Set the default command to run the Java application
ENTRYPOINT ["java", "-Xmx2048M", "-jar", "/application.jar"]