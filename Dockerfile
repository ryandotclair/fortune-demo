FROM bitnami/java:1.8.242-ol-7-r0-prod

LABEL Owner="Ryan Clair"

ADD target/fortune-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-Xmx512m", "-jar", "/app.jar"]
