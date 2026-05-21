FROM europe-north1-docker.pkg.dev/cgr-nav/pull-through/nav.no/jre:openjdk-21@sha256:ed2d694c609c54964dad1c10e92c32b153830e2c1fa56d9e8cb78113fcfc89f7

ENV APPLICATION_NAME=tilleggsstonader-arena

EXPOSE 8080
COPY build/libs/app.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
