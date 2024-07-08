# Design and Implementation Considerations

## Overview

This document describes the design and implementation considerations for our Java application. We will detail the reasoning behind our choice of technologies, the architecture of the application, and the deployment strategy.

## Technology Choices

#### Data Consuming: Apache Kafka

##### Reason for Choice:
Apache Kafka was chosen for data consumption because it excels in handling real-time data feeds and is ideal for scenarios where data does not need to be processed synchronously. Kafka provides a robust and scalable solution that can handle high throughput with low latency, making it perfect for our application’s needs. It also supports distributed data streams, which aligns with our goal of building a scalable and resilient system.

##### Alternative Considered:
We considered using traditional message brokers like RabbitMQ. However, RabbitMQ is more suitable for scenarios requiring immediate processing and acknowledgment, which is not a primary requirement for our application. Kafka’s ability to store streams of records in a fault-tolerant manner and its excellent scalability made it the preferred choice.

#### Data Storage: PostgreSQL

##### Reason for Choice:
PostgreSQL was selected as the database management system due to its scalability, stability, and comprehensive feature set. It is an open-source relational database that provides robust performance and data integrity, which are crucial for our application’s requirements. PostgreSQL’s support for advanced data types and its strong adherence to SQL standards ensure that it can handle the complex queries and operations needed by our application.

##### Alternative Considered:
We evaluated other database systems such as MySQL and MongoDB. While MySQL is a strong contender, PostgreSQL’s advanced features, such as support for JSONB and more complex queries, made it a better fit. MongoDB, being a NoSQL database, did not meet our need for relational data storage and complex transaction management.

#### Programming Language: Java 21

##### Reason for Choice:
We chose the latest version of Java (Java 21) to leverage its modern language features, performance improvements, and long-term support. Java is a well-established language with a vast ecosystem, making it a reliable choice for building and maintaining our application.

#### Containerization: Docker

##### Reason for Choice:
Docker was chosen to containerize the application to ensure a consistent runtime environment across different deployment stages. Docker facilitates the creation of isolated environments, which helps in managing dependencies and versions effectively. It also simplifies the deployment process and enhances the scalability of the application.

#### Deployment Orchestration: Docker Compose

##### Reason for Choice:
Docker Compose was used to define and manage multi-container Docker applications. It allows us to configure the services (application, Kafka, and PostgreSQL) in a single YAML file, simplifying the orchestration and deployment process. Docker Compose makes it easier to set up the development environment, run tests, and deploy the application in a consistent manner.

### Implementation Details

#### Docker Configuration

We created a Dockerfile to containerize the Java application. This Dockerfile includes the necessary instructions to build the application image, such as setting up the base image, copying the application files, and running the application.
```dockerfile
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
```
#### Dockerfile

We used Docker Compose to manage and run multi-container Docker applications. The docker-compose.yml file defines the services required for the application, including Kafka and PostgreSQL, and sets up the necessary configurations for each service.
```yaml
version: '3.8'

services:
  java-app:
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - "8080:8080"
    networks:
      - nba-statistic-network
    depends_on:
      - postgres
      - kafka

  postgres:
    image: postgres:13
    environment:
      POSTGRES_USER: user
      POSTGRES_PASSWORD: password
      POSTGRES_DB: mydatabase
    ports:
      - "5432:5432"
    networks:
      - nba-statistic-network

  kafka:
    image: confluentinc/cp-kafka:7.0.1
    depends_on:
      - zookeeper
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:9092
      KAFKA_LISTENERS: PLAINTEXT://0.0.0.0:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
      KAFKA_AUTO_CREATE_TOPICS_ENABLE: "true"
    ports:
      - "9092:9092"
    networks:
      - nba-statistic-network

  zookeeper:
    image: confluentinc/cp-zookeeper:7.0.1
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
    networks:
      - nba-statistic-network

networks:
  nba-statistic-network:
    driver: bridge
```
