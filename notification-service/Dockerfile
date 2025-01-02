FROM openjdk:8-jdk-alpine

WORKDIR /app

ADD target/notification-service-0.0.1-SNAPSHOT.jar .

EXPOSE 8103

ENTRYPOINT ["java", "-jar", "/app/notification-service-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=prod"]