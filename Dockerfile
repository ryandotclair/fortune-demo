FROM openjdk:8-jre-alpine

LABEL Owner="Ryan Clair"

RUN apk add --no-cache curl

ADD target/fortune-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-Xmx512m", "-jar", "/app.jar"]
