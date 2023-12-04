# syntax=docker/dockerfile:1.0-experimental

# Build stage
FROM openjdk:17-jdk-slim AS build

WORKDIR /workspace

COPY . .

RUN --mount=type=cache,target=/root/.m2/repository mvn -e -B clean package -Dmaven.test.skip=true

# Package stage
FROM openjdk:17-jdk-slim

COPY --from=build /workspace/target/challange6-0.0.1-SNAPSHOT.jar /usr/local/lib/demo.jar

EXPOSE 8080

CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/usr/local/lib/demo.jar"]
