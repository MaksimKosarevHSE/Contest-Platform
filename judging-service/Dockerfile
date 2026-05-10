FROM eclipse-temurin:21-jdk-jammy
RUN apt-get update \
    && apt-get install -y --no-install-recommends g++ \
    && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY target/app.jar app.jar
EXPOSE 8002
ENTRYPOINT ["java", "-jar", "app.jar"]
