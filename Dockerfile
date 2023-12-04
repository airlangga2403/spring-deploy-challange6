#
# Build stage
#
FROM openjdk:17-jdk-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN ./mvnw package


#
# Package stage
#
FROM openjdk:17-jdk-slim
COPY --from=build /home/app/target/getyourway-0.0.1-SNAPSHOT.jar /usr/local/lib/demo.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/demo.jar"]