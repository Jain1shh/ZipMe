# Stage 1: Build the application using JDK 21
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copy necessary files
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
RUN chmod +x mvnw

COPY src src

# Build using Maven Wrapper
RUN ./mvnw -B package -DskipTests

# Stage 2: Run the built application using JRE 21 (lighter)
FROM eclipse-temurin:21-jre
WORKDIR /app
VOLUME /tmp

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
