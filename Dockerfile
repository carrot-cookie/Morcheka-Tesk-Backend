FROM bellsoft/liberica-openjre-alpine:11
EXPOSE 8080

WORKDIR /test-task

COPY build/libs/*.jar backend.jar

ENTRYPOINT ["java", "-jar", "backend.jar"]
