FROM docker.io/library/maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

COPY pom.xml .
RUN mvn -B -q -DskipTests dependency:go-offline

COPY src ./src
RUN mvn -B -q -DskipTests package \
    && cp target/*.jar /app/app.jar

FROM docker.io/library/eclipse-temurin:17-jre-alpine
WORKDIR /app

ENV JAVA_TOOL_OPTIONS="-Xms128m -Xmx256m -XX:MaxMetaspaceSize=128m -Duser.timezone=UTC" \
    SERVER_PORT=8080 \
    SPRING_PROFILES_ACTIVE=prod

RUN adduser -D -u 10001 appuser
USER appuser

COPY --from=build /app/app.jar /app/app.jar
EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --retries=5 \
    CMD wget -q -O - "http://127.0.0.1:${SERVER_PORT}/actuator/health" >/dev/null 2>&1 || exit 1

ENTRYPOINT ["java","-jar","/app/app.jar"]
    