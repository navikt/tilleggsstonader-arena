FROM gcr.io/distroless/java21:nonroot

ENV APPLICATION_NAME=tilleggsstonader-arena

EXPOSE 8080
COPY build/libs/app.jar /app.jar

ENV JAVA_OPTS="-XX:MaxRAMPercentage=75"

ENTRYPOINT ["java", "-jar", "/app.jar"]
