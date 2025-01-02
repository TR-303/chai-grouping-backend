FROM openjdk:8-jdk-alpine

WORKDIR /app

ADD target/task-service-0.0.1-SNAPSHOT.jar .

EXPOSE 8104

ENTRYPOINT ["java", "-jar", "/app/task-service-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=prod"]