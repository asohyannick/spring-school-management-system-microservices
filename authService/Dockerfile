FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

COPY src ./src
RUN ./mvnw clean package -DskipTests -B

# ─────────────────────────────────────────────
# Stage 2: Runtime
# ─────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine AS runtime

# Security: run as non-root user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

# Give ownership to non-root user
RUN chown appuser:appgroup app.jar

USER appuser

EXPOSE 8080

# JVM tuning for containers:
# -XX:+UseContainerSupport      → respect Docker memory limits
# -XX:MaxRAMPercentage=75.0     → use 75% of container RAM for heap
# -Djava.security.egd           → faster startup (avoids /dev/random blocking)
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]