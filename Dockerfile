FROM openjdk:17-jdk-slim

# Download Maven 3.9.x binary
RUN wget https://dlcdn.apache.org/maven/maven-3/3.9.0/binaries/apache-maven-3.9.0-bin.tar.gz

# Extract archive
RUN tar -xzf apache-maven-3.9.0-bin.tar.gz

# Set environment variable
ENV MAVEN_HOME /apache-maven-3.9.0

# Add Maven bin directory to PATH
ENV PATH ${MAVEN_HOME}/bin:${PATH}

# Copy files and build
COPY src /home/app/src
COPY pom.xml /home/app
RUN ${MAVEN_HOME}/bin/mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM openjdk:17-jdk-slim
COPY --from=build /home/app/target/getyourway-0.0.1-SNAPSHOT.jar /usr/local/lib/demo.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/demo.jar"]