FROM eclipse-temurin:11-alpine AS builder

WORKDIR /workspace/builder

COPY gradlew .
COPY gradlew.bat .
COPY settings.gradle .
COPY build.gradle .
COPY gradle gradle
COPY src src

RUN chmod +x ./gradlew
RUN ./gradlew bootJar

FROM eclipse-temurin:11-alpine

WORKDIR /workspace/app

COPY --from=builder /workspace/builder/build/libs/*.jar app.jar
COPY docker-entrypoint.sh .

ENTRYPOINT ["sh", "docker-entrypoint.sh"]
