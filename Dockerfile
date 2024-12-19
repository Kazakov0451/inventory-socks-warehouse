FROM openjdk:17-jdk-slim AS GARDLEW_BUILD
COPY ./ ./
RUN ./gradlew clean build -x test
FROM openjdk:17-jdk-slim
COPY --from=GARDLEW_BUILD /build/libs/inventory-socks-warehouse-0.0.1-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]