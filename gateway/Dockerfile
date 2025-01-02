FROM openjdk:8-jdk-alpine

WORKDIR /app

ADD target/gateway-0.0.1-SNAPSHOT.jar .

EXPOSE 8101

ENTRYPOINT ["java", "-jar", "/app/gateway-0.0.1-SNSNAPSHOT.jar","--spring.profiles.active=prod"]