FROM europe-north1-docker.pkg.dev/cgr-nav/pull-through/nav.no/jre:openjdk-21@sha256:0401393b18aa1caa0c0f22660c3769bdb99cdc0df6a88f00a8e17ccf5490bff9

ENV APPLICATION_NAME=tilleggsstonader-arena

EXPOSE 8080
COPY build/libs/app.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
