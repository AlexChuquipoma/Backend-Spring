# Multi-stage build para Spring Boot con Maven

# Etapa 1:# ==========================================
# INSTRUCCIONES PARA RENDER (LA NUBE)
# Este archivo le dice a Render cómo crear tu servidor desde cero.
# No afecta a tu PC local.
# ==========================================

# 1. Fase de Construcción (Maven)
# Usamos una imagen con Maven para compilar el código Java
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copiar solo pom.xml primero para aprovechar caché de Docker
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar el resto del código fuente
COPY src ./src

# Compilar la aplicación
RUN mvn clean package -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Crear usuario no-root para seguridad
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copiar el JAR desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Exponer puerto
EXPOSE 8080

# Variables de entorno por defecto
ENV SPRING_PROFILES_ACTIVE=prod

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
