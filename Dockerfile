#
# Build stage
#
FROM openjdk:17-jdk-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app

# Install Maven
RUN apt-get update && \
    apt-get install -y maven

RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM openjdk:17-jdk-slim
COPY --from=build /home/app/target/challenge6-0.0.1-SNAPSHOT.jar /usr/local/lib/demo.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/usr/local/lib/demo.jar"]
