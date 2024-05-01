# Use maven images as builder
FROM maven:3.8.4-openjdk-17 as builder
 
# Copy the project files to the container
COPY . /usr/src/app
 
# Set the working directory
WORKDIR /usr/src/app
 
# Build the project and package the application
RUN mvn clean package -DskipTests
 
# Use OpenJDK 17 for running the application
FROM openjdk:17-jdk
 
# Copy the built jar file from the builder stage
COPY --from=builder /usr/src/app/target/*.jar /app/app.jar
 
# Set the command to run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]