# Use an official Maven image with OpenJDK 21 for the build stage
FROM maven:3.9.8-amazoncorretto-21 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml file and the src directory to the container
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY . .

# Run the Maven build to compile the application and package it
RUN mvn package -DskipTests

# Use an official OpenJDK 21 runtime for the runtime stage
FROM amazoncorretto:21

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the build stage to the runtime stage
COPY --from=build /app/target/nba-statisticDomain-1.0-SNAPSHOT.jar /app/nba-statisticDomain-1.0-SNAPSHOT.jar

# Expose the application port (optional, depends on your app)
EXPOSE 8080

# Specify the command to run the application
CMD ["java", "-jar", "/app/nba-statisticDomain-1.0-SNAPSHOT.jar"]