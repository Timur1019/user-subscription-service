
FROM eclipse-temurin:17-jdk-jammy as builder

WORKDIR /app
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

RUN chmod +x gradlew && ./gradlew clean build -x test --no-daemon

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app
COPY --from=builder /app/build/libs/*-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]