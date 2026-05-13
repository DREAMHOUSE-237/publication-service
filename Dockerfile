# ── Stage 1 : Build Maven ──────────────────────────────────────────
FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /app

# Copie le pom et télécharge les dépendances d'abord (cache Docker)
COPY pom.xml .
RUN apt-get update && apt-get install -y maven && \
    mvn dependency:go-offline -B

# Copie le source et compile
COPY src ./src
RUN mvn clean package -DskipTests -B

# ── Stage 2 : Image finale légère ─────────────────────────────────
FROM eclipse-temurin:17-jre-jammy

VOLUME /tmp

COPY --from=builder /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]