# Multi-stage build for Spring Boot (Java 21)
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /workspace

# copy maven files first for caching
COPY pom.xml .
COPY src ./src

# Build the application (skip tests to speed up; remove -DskipTests for CI builds)
RUN mvn -B -DskipTests package

FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

# Copy fat jar produced by spring-boot-maven-plugin
ARG JAR_FILE=target/api-0.0.1-SNAPSHOT.jar
COPY --from=build /workspace/${JAR_FILE} /app/app.jar

# Recommended JVM opts: tune in production
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:MaxRAMPercentage=75.0"
EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
  CMD wget --no-verbose --spider --timeout=2 http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
