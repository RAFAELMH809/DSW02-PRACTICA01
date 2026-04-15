FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /workspace

# Copy the pom first to maximize Docker layer cache for dependencies.
COPY pom.xml ./
RUN mvn -B -ntp dependency:go-offline

# Copy sources and build the executable Spring Boot JAR.
COPY src ./src
RUN mvn -B -ntp clean package -DskipTests

FROM eclipse-temurin:17-jre AS runtime
WORKDIR /app

# Copy only the runtime artifact from the build stage.
COPY --from=build /workspace/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
