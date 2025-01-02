FROM openjdk:8-jdk-alpine

WORKDIR /app

ADD target/user-service-0.0.1-SNAPSHOT.jar .

EXPOSE 8102

ENTRYPOINT ["java", "-jar", "/app/user-service-0.0.1-SNSNAPSHOT.jar","--spring.profiles.active=prod"]