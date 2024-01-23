FROM ghcr.io/navikt/baseimages/temurin:21

ENV APPLICATION_NAME=tilleggsstonader-arena

EXPOSE 8080
COPY build/libs/*.jar ./
COPY /.nais/init.sh /init-scripts/init.sh

ENV JAVA_OPTS="-XX:MaxRAMPercentage=75"
