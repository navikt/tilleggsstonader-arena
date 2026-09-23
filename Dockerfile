FROM europe-north1-docker.pkg.dev/cgr-nav/pull-through/nav.no/jre:openjdk-21@sha256:59aae400feb438475ef6c16275ee8a8f401707e6a09826430c01fa7450389452

ENV APPLICATION_NAME=tilleggsstonader-arena

EXPOSE 8080
COPY build/libs/app.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
