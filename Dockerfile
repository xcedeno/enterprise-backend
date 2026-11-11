# ==========================================
# ETAPA 1: Compilación y Empaquetado (Builder)
# ==========================================
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /build

# Copiar el wrapper de Maven y la configuración del proyecto
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Descargar dependencias en caché para acelerar builds posteriores
RUN chmod +x ./mvnw && ./mvnw dependency:go-offline -B

# Copiar el código fuente y empaquetar el JAR ejecutable
COPY src/ ./src/
RUN ./mvnw clean package -DskipTests -B

# ==========================================
# ETAPA 2: Imagen Final Ultraligera (Runtime)
# ==========================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Crear usuario sin privilegios de root por seguridad corporativa
RUN addgroup -S enterprise && adduser -S appuser -G enterprise
USER appuser:enterprise

# Copiar únicamente el archivo JAR generado desde la etapa de compilación
COPY --from=builder --chown=appuser:enterprise /build/target/enterprise-backend-*.jar app.jar

# Variables de entorno por defecto
ENV SERVER_PORT=8080 \
    SPRING_PROFILES_ACTIVE=prod

EXPOSE 8080

# Parámetros optimizados para contenedores en la nube (Cloud Run / GKE)
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]

