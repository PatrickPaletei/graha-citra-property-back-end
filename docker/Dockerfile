# Stage 1: Build the application
FROM maven:3.8.7-eclipse-temurin-17 AS build
COPY . /home/app
WORKDIR /home/app
RUN mvn clean package -DskipTests

# Stage 2: Package the application
FROM eclipse-temurin:17-jre-alpine
EXPOSE 5123
COPY --from=build /home/app/target/Citra-Graha-Property-0.0.1-SNAPSHOT.jar app.jar

# Set environment variables
ENV SPRING_APPLICATION_NAME=Citra-Graha-Property \
    SERVER_PORT=5123 \
    SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/graha_citra_db \
    SPRING_DATASOURCE_USERNAME=postgres \
    SPRING_DATASOURCE_PASSWORD=123456 \
    SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver \
    SPRING_DATASOURCE_MAXIMUM_POOL_SIZE=10 \
    SPRING_JPA_PROPERTIES_HIBERNATE_FORMAT_SQL=true \
    SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT=org.hibernate.dialect.PostgreSQLDialect \
    SPRING_JPA_PROPERTIES_HIBERNATE_DDL_AUTO=update \
    SPRING_JPA_SHOW_SQL=true \
    SPRING_JPA_GENERATE_DDL=true \
    SPRING_SERVLET_MULTIPART_ENABLED=true \
    SPRING_SERVLET_MULTIPART_FILE_SIZE_THRESHOLD=2KB \
    SPRING_SERVLET_MULTIPART_MAX_FILE_SIZE=200MB \
    SPRING_SERVLET_MULTIPART_MAX_REQUEST_SIZE=215MB \
    FILE_UPLOAD_DIR_PRODUCT_PHOTO=/upload/product \
    FILE_UPLOAD_DIR_WORK_EXP=/upload/work_exp

# Create directories for file uploads
RUN mkdir -p /upload/product /upload/work_exp

ENTRYPOINT ["java", "-jar", "/app.jar"]
