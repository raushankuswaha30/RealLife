# Stage 1: Build the JAR package using Gradle with Java 21
FROM gradle:8.7-jdk21 AS build
WORKDIR /app
COPY . .
WORKDIR /app/RealLife
RUN ./gradlew bootJar --no-daemon -x test

# Stage 2: Create a lightweight runtime image with Java 21 JRE
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/RealLife/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
