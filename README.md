# 🎓 Portfolio Backend - Spring Boot

Backend API REST para portafolio de programadores desarrollado con Spring Boot, JWT, PostgreSQL y Docker.

## 🚀 Características

- ✅ Autenticación JWT
- ✅ Gestión de usuarios y programadores
- ✅ Sistema de asesorías
- ✅ Portafolio de proyectos
- ✅ Base de datos PostgreSQL (producción) / H2 (desarrollo)
- ✅ Docker ready
- ✅ Despliegue en Render

## 📋 Requisitos

- Java 21+
- Maven 3.9+ (o usar `mvnw` incluido)
- Docker (opcional, para desarrollo local con PostgreSQL)

## 🛠️ Instalación y Ejecución

### Opción 1: Desarrollo Rápido (H2)

```bash
# Clonar el repositorio
git clone <tu-repo>
cd backend-spring

# Ejecutar con perfil dev (H2 en memoria)
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev

# O en Linux/Mac
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

La aplicación estará disponible en: `http://localhost:8080`

Consola H2: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:portfolio_db`
- Username: `sa`
- Password: *(dejar en blanco)*

### Opción 2: Desarrollo con PostgreSQL (Docker)

```bash
# Iniciar PostgreSQL con Docker Compose
docker-compose up -d postgres

# Ejecutar la aplicación con perfil prod
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=prod
```

### Opción 3: Docker Completo

```bash
# Construir y ejecutar todo con Docker Compose
docker-compose up --build

# Detener servicios
docker-compose down

# Detener y eliminar volúmenes (resetear BD)
docker-compose down -v
```

## 📁 Estructura del Proyecto

```
backend-spring/
├── src/
│   ├── main/
│   │   ├── java/com/portfolio/backend/
│   │   │   ├── config/          # Configuración (Security, etc.)
│   │   │   ├── controller/      # Endpoints REST
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── entity/          # Entidades JPA
│   │   │   ├── repository/      # Repositorios JPA
│   │   │   ├── security/        # JWT y filtros de seguridad
│   │   │   └── service/         # Lógica de negocio
│   │   └── resources/
│   │       ├── application.yml        # Config base
│   │       ├── application-dev.yml    # Config desarrollo (H2)
│   │       └── application-prod.yml   # Config producción (PostgreSQL)
│   └── test/                    # Tests
├── Dockerfile                   # Imagen Docker
├── docker-compose.yml          # Orquestación local
├── DEPLOYMENT.md               # Guía de despliegue en Render
└── pom.xml                     # Dependencias Maven
```

## 🔐 API Endpoints

### Autenticación

**Registrar usuario**
```bash
POST /api/auth/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "role": "PROGRAMMER"
}
```

**Login**
```bash
POST /api/auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "password123"
}
```

**Obtener usuario actual**
```bash
GET /api/auth/me
Authorization: Bearer <token>
```

### Próximos Endpoints (En desarrollo)

- `/api/projects` - CRUD de proyectos
- `/api/advisories` - Gestión de asesorías
- `/api/programmers` - Perfiles de programadores
- `/api/dashboard` - Estadísticas
- `/api/reports` - Reportes PDF/Excel

## 🔧 Variables de Entorno

### Desarrollo Local
```bash
SPRING_PROFILES_ACTIVE=dev
```

### Producción (Render)
```bash
SPRING_PROFILES_ACTIVE=prod
DATABASE_URL=jdbc:postgresql://host:5432/dbname
DB_USERNAME=postgres
DB_PASSWORD=yourpassword
JWT_SECRET=your-secret-key-min-256-bits
JWT_EXPIRATION=86400000
PORT=8080
```

## 🚢 Despliegue en Render

Ver guía completa en [DEPLOYMENT.md](DEPLOYMENT.md)

**Resumen rápido:**
1. Crear PostgreSQL Database en Render
2. Crear Web Service (Docker)
3. Configurar variables de entorno
4. Conectar repositorio Git
5. Deploy automático

## 🧪 Testing

```bash
# Compilar sin tests
.\mvnw.cmd clean compile -DskipTests

# Compilar y empaquetar
.\mvnw.cmd clean package -DskipTests

# Ejecutar tests
.\mvnw.cmd test
```

## 📚 Tecnologías Utilizadas

- **Framework**: Spring Boot 4.0.2
- **Java**: 21
- **Security**: Spring Security + JWT (jjwt 0.12.6)
- **Database**: H2 (dev), PostgreSQL (prod)
- **ORM**: Spring Data JPA + Hibernate
- **Build**: Maven
- **Containerization**: Docker
- **Deployment**: Render

## 👥 Roles de Usuario

- `USER` - Usuario regular (puede solicitar asesorías)
- `PROGRAMMER` - Programador (puede ofrecer asesorías y proyectos)
- `ADMIN` - Administrador (acceso total)

## 📝 Notas de Desarrollo

### Cambiar perfil en runtime
```bash
# Usando variable de entorno
set SPRING_PROFILES_ACTIVE=prod
.\mvnw.cmd spring-boot:run

# Usando argumento
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=prod
```

### Resetear base de datos H2
Simplemente reinicia la aplicación (H2 está en modo `create-drop`)

### Ver logs detallados
En `application-dev.yml` ya está configurado el nivel `DEBUG` para desarrollo

## 🐛 Problemas Comunes

**Error: Maven no encontrado**
```bash
# Usa el wrapper incluido
.\mvnw.cmd     # Windows
./mvnw         # Linux/Mac
```

**Error: Puerto 8080 ocupado**
```bash
# Cambiar puerto
set PORT=8081
.\mvnw.cmd spring-boot:run
```

**Error: Conexión a PostgreSQL rechazada**
```bash
# Verificar que Docker esté corriendo
docker ps

# Reiniciar PostgreSQL
docker-compose restart postgres
```

## 📖 Próximos Pasos

- [ ] Implementar controladores de proyectos y asesorías
- [ ] Sistema de notificaciones por email
- [ ] Reportes PDF/Excel
- [ ] Dashboard con estadísticas
- [ ] Documentación Swagger/OpenAPI
- [ ] Tests unitarios e integración

## 📄 Licencia

Proyecto académico - Universidad

## 👨‍💻 Autor

Desarrollado como proyecto final de Desarrollo Web
