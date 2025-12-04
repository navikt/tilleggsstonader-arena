FROM europe-north1-docker.pkg.dev/cgr-nav/pull-through/nav.no/jre:openjdk-21@sha256:7c276b192973ff746af32029005af63ed4dab4f1ec9c9e06bbbb4a35c747323a

ENV APPLICATION_NAME=tilleggsstonader-arena

EXPOSE 8080
COPY build/libs/app.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
