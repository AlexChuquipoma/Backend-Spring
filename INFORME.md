# Programación y Plataformas Web

## Portfolio Web - Backend

<div align="center">

![Universidad Politécnica Salesiana](https://bioquimicayfarmaciagye.ups.edu.ec/image/company_logo?img_id=95813&t=1739897459404)

### Universidad Politécnica Salesiana
### Carrera de Computación

<br>

### Autores
**Alexander Chuquipoma**  
📧 [achuquipoma@est.ups.edu.ec](mailto:achuquipoma@est.ups.edu.ec)  
💻 GitHub: [AlexChuquipoma](https://github.com/AlexChuquipoma)

**Juan Fernandez**  
📧 [jfernandezl6@est.ups.edu.ec](mailto:jfernandezl6@est.ups.edu.ec)  
💻 GitHub: [Juan0Fernandez](https://github.com/Juan0Fernandez)

---

# INFORME COMPLETO DEL PROYECTO - CiberPortfolio

## Tabla de Contenidos

1. [Descripcion General del Proyecto](#1-descripcion-general-del-proyecto)
2. [Arquitectura del Sistema](#2-arquitectura-del-sistema)
3. [Backend - Spring Boot](#3-backend---spring-boot)
   - 3.1 [Tecnologias Utilizadas](#31-tecnologias-utilizadas)
   - 3.2 [Estructura de Carpetas](#32-estructura-de-carpetas)
   - 3.3 [Seguridad y Autenticacion (JWT)](#33-seguridad-y-autenticacion-jwt)
   - 3.4 [Modulo de Usuarios](#34-modulo-de-usuarios)
   - 3.5 [Modulo de Perfiles de Programador](#35-modulo-de-perfiles-de-programador)
   - 3.6 [Modulo de Proyectos](#36-modulo-de-proyectos)
   - 3.7 [Modulo de Horarios (Schedules)](#37-modulo-de-horarios-schedules)
   - 3.8 [Modulo de Asesorias (Advisories)](#38-modulo-de-asesorias-advisories)
   - 3.9 [Panel de Administrador](#39-panel-de-administrador)
4. [Integraciones Externas](#4-integraciones-externas)
   - 4.1 [Cloudinary - Gestion de Imagenes](#41-cloudinary---gestion-de-imagenes)
   - 4.2 [Brevo - Envio de Correos Electronicos](#42-brevo---envio-de-correos-electronicos)
5. [Frontend - Astro](#5-frontend---astro)
6. [Conexion Frontend-Backend](#6-conexion-frontend-backend)
7. [Despliegue y Configuracion](#7-despliegue-y-configuracion)
   - 7.1 [Render (Backend)](#71-render-backend)
   - 7.2 [Firebase Hosting (Frontend)](#72-firebase-hosting-frontend)
8. [Archivos Importantes](#8-archivos-importantes)

---

## 1. Descripcion General del Proyecto

**CiberPortfolio** es una plataforma web que permite a programadores mostrar sus proyectos (portafolio) y ofrecer asesorias a usuarios interesados. El sistema tiene tres roles principales:

| Rol | Descripcion |
|-----|-------------|
| **USER** | Usuario registrado que puede ver perfiles, proyectos y solicitar asesorias |
| **PROGRAMMER** | Programador que gestiona su portafolio, horarios y asesorias |
| **ADMIN** | Administrador que gestiona usuarios, horarios y reportes |

**Flujo general**: Un usuario se registra (siempre como `USER`), un administrador puede cambiar su rol a `PROGRAMMER`. El programador crea su perfil profesional, sube proyectos y configura horarios de disponibilidad. Otros usuarios pueden ver estos portafolios y solicitar asesorias en los horarios disponibles.

---

## 2. Arquitectura del Sistema

```
+---------------------------+         +---------------------------+
|     FRONTEND (Astro)      |         |   BACKEND (Spring Boot)   |
|   Firebase Hosting        | ------> |   Render.com              |
|   Puerto: 4321 (dev)      |  HTTP   |   Puerto: 8080            |
|   TypeScript               |  REST   |   Java 21                 |
+---------------------------+         +---------------------------+
                                              |           |
                                              v           v
                                      +-----------+  +-----------+
                                      | PostgreSQL |  | Cloudinary|
                                      | (Render)   |  | (Imagenes)|
                                      +-----------+  +-----------+
                                              |
                                              v
                                      +-----------+
                                      |   Brevo   |
                                      |  (Emails) |
                                      +-----------+
```

- **Frontend**: Astro 5.17.1 con TypeScript, desplegado en Firebase Hosting
- **Backend**: Spring Boot 4.0.2 con Java 21, desplegado en Render.com
- **Base de Datos**: PostgreSQL (produccion en Render), H2 (desarrollo local)
- **Imagenes**: Cloudinary (almacenamiento en la nube)
- **Correos**: Brevo API (notificaciones por email)
- **Comunicacion**: API REST con autenticacion JWT

---

## 3. Backend - Spring Boot

### 3.1 Tecnologias Utilizadas

| Tecnologia | Version | Uso |
|------------|---------|-----|
| Java | 21 | Lenguaje principal |
| Spring Boot | 4.0.2 | Framework backend |
| Spring Security | Incluido | Seguridad y autenticacion |
| Spring Data JPA | Incluido | Acceso a base de datos |
| Hibernate | Incluido | ORM (mapeo objeto-relacional) |
| PostgreSQL | Ultima | Base de datos en produccion |
| H2 Database | Ultima | Base de datos en desarrollo |
| JJWT | 0.12.6 | Generacion y validacion de tokens JWT |
| Lombok | Ultima | Reduccion de codigo boilerplate |
| Cloudinary SDK | 1.39.0 | Subida de imagenes |
| Maven | 3.9 | Gestor de dependencias |
| Docker | Multi-stage | Contenedorizacion para despliegue |

### 3.2 Estructura de Carpetas

```
src/main/java/com/portfolio/backend/
|
|-- config/
|   |-- CloudinaryConfig.java          # Configuracion de Cloudinary
|
|-- security/
|   |-- config/
|   |   |-- SecurityConfig.java        # Configuracion de Spring Security y CORS
|   |-- filters/
|   |   |-- JwtAuthenticationFilter.java # Filtro que intercepta cada request
|   |-- services/
|   |   |-- AuthService.java           # Logica de login y registro
|   |   |-- CustomUserDetailsService.java # Carga usuario desde BD
|   |-- utils/
|   |   |-- JwtUtil.java               # Generacion y validacion de tokens JWT
|   |-- dtos/
|       |-- LoginRequest.java          # DTO para login
|       |-- RegisterRequest.java       # DTO para registro
|       |-- AuthResponse.java          # DTO de respuesta con token
|
|-- users/
|   |-- entity/
|   |   |-- User.java                  # Entidad de usuario
|   |   |-- enums/Role.java            # Enum: USER, PROGRAMMER, ADMIN
|   |-- repository/UserRepository.java
|   |-- services/UserService.java / UserServiceImpl.java
|   |-- controllers/UserController.java
|   |-- dtos/UserDTO.java, UpdateUserRequest.java
|
|-- profiles/
|   |-- entity/ProgrammerProfile.java  # Perfil profesional del programador
|   |-- repository/ProgrammerProfileRepository.java
|   |-- services/ProgrammerProfileService.java / ProgrammerProfileServiceImpl.java
|   |-- controllers/ProgrammerProfileController.java
|   |-- dtos/ProgrammerProfileDTO.java, UpdateProfileRequest.java
|
|-- projects/
|   |-- entity/Project.java            # Entidad de proyecto
|   |-- entity/enums/ProjectType.java  # Enum: PERSONAL, ACADEMIC, etc.
|   |-- repository/ProjectRepository.java
|   |-- services/ProjectService.java / ProjectServiceImpl.java
|   |-- controllers/ProjectController.java
|   |-- dtos/CreateProjectRequest.java, UpdateProjectRequest.java, ProjectDTO.java
|
|-- schedules/
|   |-- entity/Schedule.java           # Entidad de horario
|   |-- entity/enums/Modality.java     # Enum: VIRTUAL, PRESENCIAL
|   |-- repository/ScheduleRepository.java
|   |-- services/ScheduleService.java / ScheduleServiceImpl.java
|   |-- controllers/ScheduleController.java
|   |-- dto/ScheduleDTO.java
|
|-- advisories/
|   |-- entity/Advisory.java           # Entidad de asesoria
|   |-- entity/enums/AdvisoryStatus.java # Enum: PENDING, ACCEPTED, REJECTED, COMPLETED
|   |-- repository/AdvisoryRepository.java
|   |-- services/AdvisoryService.java / AdvisoryServiceImpl.java
|   |-- controllers/AdvisoryController.java
|   |-- dto/AdvisoryDTO.java
|
|-- emails/
|   |-- service/EmailService.java      # Interfaz
|   |-- service/EmailServiceImpl.java  # Implementacion con Brevo API
|
|-- exceptions/
    |-- GlobalExceptionHandler.java    # Manejo global de errores
```

### 3.3 Seguridad y Autenticacion (JWT)

La seguridad del sistema se implementa con **Spring Security** y **JSON Web Tokens (JWT)**. A continuacion se detalla cada componente y como trabajan juntos.

#### 3.3.1 Configuracion de Seguridad (`SecurityConfig.java`)

**Archivo**: `src/main/java/com/portfolio/backend/security/config/SecurityConfig.java`

Este archivo define las reglas de seguridad de toda la aplicacion:

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()          // Login/Register: publico
                .requestMatchers("/api/profiles/all").permitAll()     // Ver todos los perfiles: publico
                .requestMatchers("/api/profiles/user/**").permitAll() // Ver perfil por ID: publico
                .requestMatchers(GET, "/api/projects/**").permitAll() // Ver proyectos: publico
                .requestMatchers("/api/schedules/**").permitAll()     // Horarios: publico
                .anyRequest().authenticated())                        // Todo lo demas: requiere JWT
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
```

**Lo que hace cada linea**:
- `csrf.disable()`: Desactiva CSRF porque usamos JWT (no cookies de sesion)
- `SessionCreationPolicy.STATELESS`: No se crean sesiones en el servidor, cada request debe traer su token JWT
- `permitAll()`: Endpoints publicos que no requieren autenticacion
- `authenticated()`: Todo lo que no esta en la lista publica requiere un token JWT valido
- `addFilterBefore(jwtAuthenticationFilter)`: Agrega el filtro JWT antes del filtro de autenticacion por defecto

**CORS** - Origenes permitidos:
```java
config.setAllowedOrigins(List.of(
    "http://localhost:4321",                          // Frontend desarrollo
    "http://localhost:4322",                          // Frontend alterno
    "http://localhost:4323",                          // Frontend alterno
    "http://localhost:3000",                          // Otro frontend
    "https://backend-portfolio-f9095.web.app",        // Firebase produccion
    "https://backend-portfolio-f9095.firebaseapp.com" // Firebase alterno
));
config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
```

**Encriptacion de contrasenas**: Se usa `BCryptPasswordEncoder` para hashear las contrasenas antes de guardarlas en la base de datos. Esto garantiza que las contrasenas nunca se almacenan en texto plano.

#### 3.3.2 Utilidad JWT (`JwtUtil.java`)

**Archivo**: `src/main/java/com/portfolio/backend/security/utils/JwtUtil.java`

Esta clase se encarga de crear y validar tokens JWT:

```java
@Component
public class JwtUtil {
    private final SecretKey key;      // Clave secreta para firmar tokens
    private final long expiration;     // Tiempo de expiracion (24 horas = 86400000 ms)

    // Constructor: crea la clave HMAC-SHA a partir del secreto en application.yml
    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.expiration}") long expiration) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expiration = expiration;
    }

    // Genera un token JWT con el email y rol del usuario
    public String generateToken(String email, String role) {
        return Jwts.builder()
            .subject(email)                                          // Email como subject
            .claim("role", role)                                     // Rol como claim personalizado
            .issuedAt(new Date())                                    // Fecha de creacion
            .expiration(new Date(System.currentTimeMillis() + expiration)) // Expiracion: 24h
            .signWith(key)                                           // Firma con HMAC-SHA
            .compact();
    }

    // Valida si un token es correcto y no ha expirado
    public boolean isTokenValid(String token) {
        try {
            getClaims(token);  // Si no lanza excepcion, es valido
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
```

**Configuracion JWT** (en `application.yml`):
```yaml
jwt:
  secret: ${JWT_SECRET:CambiaEstaPorUnaClaveSecretaMuyLargaYSeguraDeAlMenos256Bits2024PortfolioBackend}
  expiration: ${JWT_EXPIRATION:86400000}  # 24 horas en milisegundos
```

En produccion, `JWT_SECRET` se configura como variable de entorno en Render con un valor seguro.

#### 3.3.3 Filtro de Autenticacion (`JwtAuthenticationFilter.java`)

**Archivo**: `src/main/java/com/portfolio/backend/security/filters/JwtAuthenticationFilter.java`

Este filtro intercepta **cada peticion HTTP** y verifica el token JWT:

```
Peticion HTTP --> JwtAuthenticationFilter --> Controlador
                        |
                        v
                  1. Lee header "Authorization: Bearer <token>"
                  2. Extrae el token (quita "Bearer ")
                  3. Valida el token con JwtUtil.isTokenValid()
                  4. Extrae el email del token
                  5. Carga el usuario desde la BD
                  6. Establece la autenticacion en SecurityContext
                  7. La peticion continua hacia el controlador
```

**Endpoints excluidos del filtro** (no necesitan token):
- `/api/auth/*` (login y registro)
- `/h2-console/*` (consola de base de datos en desarrollo)

#### 3.3.4 Flujo Completo de Login

```
1. FRONTEND envia POST /api/auth/login con { email, password }
          |
          v
2. AuthController recibe la peticion
          |
          v
3. AuthService.login():
   a. AuthenticationManager autentica con email + password
   b. BCryptPasswordEncoder compara el hash de la contrasena
   c. Si es correcto, busca al usuario en la BD
   d. Genera un token JWT con JwtUtil.generateToken(email, role)
   e. Retorna AuthResponse { id, name, email, role, token }
          |
          v
4. FRONTEND recibe el token y lo guarda en localStorage
          |
          v
5. En cada peticion posterior, el frontend envia:
   Header: "Authorization: Bearer <token>"
```

#### 3.3.5 Flujo Completo de Registro

```
1. FRONTEND envia POST /api/auth/register con { name, email, password }
          |
          v
2. AuthService.register():
   a. Verifica que el email NO exista en la BD
   b. Crea un nuevo User con Role.USER (hardcoded por seguridad)
   c. Encripta la contrasena con BCrypt
   d. Guarda el usuario en la BD
   e. Genera un token JWT
   f. Retorna AuthResponse { id, name, email, role: "user", token }
          |
          v
3. FRONTEND recibe el token, guarda en localStorage, redirige al inicio
```

**Nota de seguridad**: El rol siempre se asigna como `Role.USER` en el backend, sin importar lo que envie el frontend. Esto evita que un usuario pueda registrarse como ADMIN o PROGRAMMER directamente.

### 3.4 Modulo de Usuarios

**Entidad**: `User.java` - Tabla `users`

| Campo | Tipo | Descripcion |
|-------|------|-------------|
| id | Long (auto) | Identificador unico |
| name | String | Nombre del usuario |
| email | String (unique) | Correo electronico |
| password | String | Contrasena encriptada con BCrypt |
| imageUrl | String | URL de imagen de perfil (Cloudinary) |
| role | Role (enum) | USER, PROGRAMMER, ADMIN |
| createdAt | LocalDateTime | Fecha de creacion |
| updatedAt | LocalDateTime | Fecha de ultima actualizacion |

**Relaciones en cascada** (para eliminacion):
```java
@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Project> projects;

@OneToMany(mappedBy = "programmer", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Advisory> advisoriesAsProgrammer;

@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Advisory> advisoriesAsUser;

@OneToMany(mappedBy = "programmer", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Schedule> schedules;

@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
private ProgrammerProfile programmerProfile;
```

Esto permite que cuando un administrador elimina un usuario, se eliminen automaticamente todos sus datos relacionados (proyectos, asesorias, horarios, perfil profesional).

**Endpoints** (`/api/users`):

| Metodo | Endpoint | Descripcion | Auth |
|--------|----------|-------------|------|
| GET | `/api/users/me` | Obtener mi perfil de usuario | JWT |
| PUT | `/api/users/me` | Actualizar mi nombre/contrasena | JWT |
| POST | `/api/users/me/image` | Subir imagen de perfil | JWT |
| GET | `/api/users` | Listar todos los usuarios (admin) | JWT |
| PUT | `/api/users/{id}/role` | Cambiar rol de usuario (admin) | JWT |
| DELETE | `/api/users/{id}` | Eliminar usuario (admin) | JWT |

**Subida de imagen de perfil** - Flujo:
```
1. Frontend envia archivo de imagen via multipart/form-data
2. UserServiceImpl.updateProfileImage() recibe el archivo
3. Sube la imagen a Cloudinary con cloudinary.uploader().upload()
4. Cloudinary retorna la URL segura (https)
5. Se guarda la URL en el campo imageUrl del usuario
6. Se retorna el usuario actualizado con la nueva URL de imagen
```

**Eliminacion de usuario** - Flujo:
```
1. Admin envia DELETE /api/users/{id}
2. UserServiceImpl.deleteUser() busca el usuario por ID
3. Se ejecuta userRepository.delete(user) con @Transactional
4. JPA Cascade elimina automaticamente:
   - Todos los proyectos del usuario
   - Todas las asesorias (como programador y como usuario)
   - Todos los horarios del usuario
   - El perfil profesional del usuario
5. Retorna HTTP 204 No Content
```

### 3.5 Modulo de Perfiles de Programador

**Entidad**: `ProgrammerProfile.java` - Tabla `programmer_profiles`

| Campo | Tipo | Descripcion |
|-------|------|-------------|
| id | Long (auto) | Identificador unico |
| user | User (FK) | Relacion con el usuario |
| jobTitle | String | Titulo profesional |
| bio | String (TEXT) | Biografia/descripcion |
| skills | List<String> | Lista de habilidades |
| githubUrl | String | URL de GitHub |
| linkedinUrl | String | URL de LinkedIn |
| websiteUrl | String | URL de sitio web personal |
| yearsExperience | Integer | Anos de experiencia |
| education | String | Formacion academica |
| certifications | List<String> | Certificaciones |
| languages | List<String> | Idiomas |

**Endpoints** (`/api/profiles`):

| Metodo | Endpoint | Descripcion | Auth |
|--------|----------|-------------|------|
| GET | `/api/profiles/me` | Mi perfil profesional | JWT |
| GET | `/api/profiles/user/{userId}` | Perfil publico por ID | Publico |
| GET | `/api/profiles/all` | Todos los perfiles | Publico |
| POST | `/api/profiles` | Crear/actualizar perfil | JWT |
| PUT | `/api/profiles` | Actualizar perfil | JWT |
| DELETE | `/api/profiles` | Eliminar perfil | JWT |

**Como funciona cuando un programador crea su perfil**:
```
1. El programador (usuario con rol PROGRAMMER) accede a "Perfil Profesional"
2. Llena formulario con jobTitle, bio, skills, URLs, experiencia, etc.
3. Frontend envia POST /api/profiles con los datos
4. ProgrammerProfileServiceImpl:
   a. Busca el usuario por email (del token JWT)
   b. Verifica si ya existe un perfil para ese usuario
   c. Si existe: actualiza los campos
   d. Si no existe: crea uno nuevo
   e. Guarda en la BD y retorna el perfil como DTO
5. El perfil queda visible publicamente en /api/profiles/user/{userId}
```

### 3.6 Modulo de Proyectos

**Entidad**: `Project.java` - Tabla `projects`

| Campo | Tipo | Descripcion |
|-------|------|-------------|
| id | Long (auto) | Identificador unico |
| name | String | Nombre del proyecto |
| description | String (TEXT) | Descripcion del proyecto |
| type | ProjectType (enum) | PERSONAL, ACADEMIC, PROFESSIONAL, FREELANCE, OPEN_SOURCE |
| techs | List<String> | Tecnologias utilizadas |
| imageUrl | String | URL de imagen del proyecto |
| repoUrl | String | URL del repositorio |
| deployUrl | String | URL del despliegue |
| owner | User (FK) | Dueno del proyecto |
| createdAt | LocalDateTime | Fecha de creacion |
| updatedAt | LocalDateTime | Fecha de actualizacion |

**Endpoints** (`/api/projects`):

| Metodo | Endpoint | Descripcion | Auth |
|--------|----------|-------------|------|
| GET | `/api/projects` | Todos los proyectos | Publico |
| GET | `/api/projects/{id}` | Proyecto por ID | Publico |
| GET | `/api/projects/user/{userId}` | Proyectos de un usuario | Publico |
| GET | `/api/projects/my` | Mis proyectos | JWT |
| POST | `/api/projects` | Crear proyecto | JWT |
| PUT | `/api/projects/{id}` | Actualizar proyecto | JWT (owner) |
| DELETE | `/api/projects/{id}` | Eliminar proyecto | JWT (owner) |

**Como funciona cuando un programador crea un proyecto**:
```
1. El programador accede a la seccion "Proyectos" en su dashboard
2. Llena formulario: nombre, descripcion, tipo, tecnologias, URLs
3. Frontend envia POST /api/projects con el body JSON
4. ProjectServiceImpl.createProject():
   a. Busca el usuario autenticado por email (del token JWT)
   b. Crea entidad Project con builder pattern
   c. Asigna el usuario como owner
   d. Guarda en la BD
   e. Retorna ProjectDTO con datos del proyecto y del owner
5. El proyecto aparece en el listado publico y en el portafolio del programador
```

**Validacion de URLs**: Tanto `CreateProjectRequest` como `UpdateProjectRequest` validan que la URL del repositorio cumpla el patron `^https?://.+/.+$`, aceptando cualquier URL HTTP/HTTPS valida.

**Validacion de ownership**: Solo el dueno de un proyecto puede editarlo o eliminarlo. Esto se verifica comparando el email del token JWT con el email del owner del proyecto.

### 3.7 Modulo de Horarios (Schedules)

**Entidad**: `Schedule.java` - Tabla `schedules`

| Campo | Tipo | Descripcion |
|-------|------|-------------|
| id | Long (auto) | Identificador unico |
| programmer | User (FK) | Programador dueno del horario |
| date | LocalDate | Fecha del horario |
| time | LocalTime | Hora de inicio |
| startTime | LocalTime | Hora de inicio (columna BD) |
| endTime | LocalTime | Hora de fin (auto: time + 1 hora) |
| status | String | "AVAILABLE" o "BOOKED" |
| modality | Modality (enum) | VIRTUAL o PRESENCIAL |
| dayOfWeek | String | Dia de la semana (auto-calculado) |
| description | String | Descripcion opcional |
| createdAt | LocalDateTime | Fecha de creacion |

**`@PrePersist` - Auto-calculo de campos**:
```java
@PrePersist
protected void onCreate() {
    createdAt = LocalDateTime.now();
    if (status == null) status = "AVAILABLE";
    if (dayOfWeek == null && date != null) dayOfWeek = date.getDayOfWeek().name();
    if (startTime == null && time != null) startTime = time;
    if (endTime == null && time != null) endTime = time.plusHours(1);
}
```

Esto significa que:
- `startTime` se calcula automaticamente a partir de `time`
- `endTime` se calcula como `time + 1 hora` si no se especifica
- `dayOfWeek` se calcula desde la fecha (MONDAY, TUESDAY, etc.)

**Endpoints** (`/api/schedules`):

| Metodo | Endpoint | Descripcion | Auth |
|--------|----------|-------------|------|
| POST | `/api/schedules` | Crear horario | Publico |
| GET | `/api/schedules` | Todos los horarios | Publico |
| GET | `/api/schedules/programmer/{id}` | Horarios de un programador (solo AVAILABLE) | Publico |
| DELETE | `/api/schedules/{id}` | Eliminar horario | Publico |

**Como funciona cuando un programador crea un horario**:
```
1. El programador accede a "Mi Horario" en su dashboard
2. Selecciona fecha, hora y modalidad (Virtual/Presencial)
3. Frontend envia POST /api/schedules con:
   { programmerId, date, time, modality }
4. ScheduleServiceImpl.createSchedule():
   a. Busca el programador por ID
   b. Calcula endTime (time + 1 hora) si no se especifica
   c. Crea Schedule con status "AVAILABLE"
   d. @PrePersist calcula dayOfWeek, startTime, endTime
   e. Guarda en la BD
5. El horario aparece como disponible para que usuarios lo reserven
```

### 3.8 Modulo de Asesorias (Advisories)

**Entidad**: `Advisory.java` - Tabla `advisories`

| Campo | Tipo | Descripcion |
|-------|------|-------------|
| id | Long (auto) | Identificador unico |
| programmer | User (FK) | Programador que ofrece la asesoria |
| user | User (FK) | Usuario que solicita la asesoria |
| schedule | Schedule (FK) | Horario vinculado |
| status | AdvisoryStatus (enum) | PENDING, ACCEPTED, REJECTED, COMPLETED |
| message | String | Mensaje del usuario al solicitar |
| responseMessage | String | Respuesta del programador |
| date | LocalDate | Fecha de la asesoria |
| time | LocalTime | Hora de la asesoria |
| modality | String | VIRTUAL o PRESENCIAL |
| createdAt | LocalDateTime | Fecha de creacion |

**Endpoints** (`/api/advisories`):

| Metodo | Endpoint | Descripcion | Auth |
|--------|----------|-------------|------|
| POST | `/api/advisories` | Crear asesoria (reservar) | Autenticado |
| GET | `/api/advisories/programmer/{id}` | Asesorias de un programador | Autenticado |
| GET | `/api/advisories/user/{id}` | Asesorias de un usuario | Autenticado |
| GET | `/api/advisories/all` | Todas las asesorias | Autenticado |
| PUT | `/api/advisories/{id}/status` | Cambiar estado (aceptar/rechazar) | Autenticado |
| GET | `/api/advisories/stats/programmer/{id}` | Estadisticas del programador | Autenticado |
| GET | `/api/advisories/stats/user/{id}` | Estadisticas del usuario | Autenticado |

**Como funciona cuando un usuario agenda una asesoria**:
```
1. El usuario visita el perfil de un programador
2. Hace clic en "Agendar Asesoria"
3. Ve los horarios DISPONIBLES del programador
4. Selecciona un horario y escribe un mensaje
5. Frontend envia POST /api/advisories con:
   { programmerId, userId, scheduleId, message, date, time, modality }
6. AdvisoryServiceImpl.createAdvisory():
   a. Busca programador (User) y usuario (User) por ID
   b. Busca el Schedule por ID
   c. Verifica que el horario NO este "BOOKED"
   d. Cambia el status del Schedule a "BOOKED"
   e. Crea la Advisory con status "PENDING"
   f. Guarda en la BD
   g. ENVIA EMAIL al programador notificando la nueva solicitud
7. El programador ve la solicitud en su dashboard
```

**Flujo de estados de una asesoria**:
```
PENDING  ----[programador acepta]----> ACCEPTED ----[se completa]----> COMPLETED
   |
   +-----[programador rechaza]----> REJECTED (libera el horario)
```

**Cuando el programador responde** (`updateAdvisoryStatus`):
```
1. Programador ve solicitudes pendientes en "Solicitudes"
2. Acepta o rechaza la solicitud
3. PUT /api/advisories/{id}/status?status=ACCEPTED&responseMessage=...
4. AdvisoryServiceImpl.updateAdvisoryStatus():
   a. Cambia el status de la asesoria
   b. Si es REJECTED: libera el horario (Schedule vuelve a "AVAILABLE")
   c. Guarda cambios
   d. ENVIA EMAIL al usuario notificando la decision
```

**Estadisticas** (`getProgrammerStats` / `getUserStats`):
Retorna un mapa con contadores:
```json
{
  "total": 15,
  "pending": 3,
  "accepted": 5,
  "rejected": 2,
  "completed": 5,
  "virtual": 10,
  "presencial": 5
}
```

Estas estadisticas alimentan el dashboard del programador (seccion "Estadisticas").

### 3.9 Panel de Administrador

El administrador tiene acceso a las siguientes funcionalidades a traves de endpoints existentes:

**USUARIOS** (`/api/users`):
- Ver todos los usuarios registrados: `GET /api/users`
- Cambiar rol de un usuario (USER -> PROGRAMMER o ADMIN): `PUT /api/users/{id}/role?role=PROGRAMMER`
- Eliminar un usuario y todos sus datos: `DELETE /api/users/{id}`

**HORARIOS** (`/api/schedules`):
- Ver todos los horarios de todos los programadores: `GET /api/schedules`
- Eliminar horarios: `DELETE /api/schedules/{id}`

**REPORTES** (`/api/advisories`):
- Ver todas las asesorias: `GET /api/advisories/all`
- Ver estadisticas por programador: `GET /api/advisories/stats/programmer/{id}`

---

## 4. Integraciones Externas

### 4.1 Cloudinary - Gestion de Imagenes

**Archivo de configuracion**: `src/main/java/com/portfolio/backend/config/CloudinaryConfig.java`

Cloudinary es un servicio en la nube para almacenar y servir imagenes. Se utiliza para las imagenes de perfil de los usuarios.

**Configuracion**:
```java
@Configuration
public class CloudinaryConfig {
    @Value("${cloudinary.cloud-name}") private String cloudName;
    @Value("${cloudinary.api-key}") private String apiKey;
    @Value("${cloudinary.api-secret}") private String apiSecret;

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
            "cloud_name", cloudName,
            "api_key", apiKey,
            "api_secret", apiSecret,
            "secure", true
        ));
    }
}
```

**Variables de entorno** (en `application-prod.yml`):
```yaml
cloudinary:
  cloud-name: ${CLOUDINARY_CLOUD_NAME:dfarmcidt}
  api-key: ${CLOUDINARY_API_KEY:289235364492953}
  api-secret: ${CLOUDINARY_API_SECRET:...}
```

**Credenciales de Cloudinary**:
| Variable | Valor |
|----------|-------|
| CLOUDINARY_CLOUD_NAME | `dfarmcidt` |
| CLOUDINARY_API_KEY | `289235364492953` |
| CLOUDINARY_API_SECRET | Configurado en variables de entorno de Render |

**Como se usa en el codigo** (`UserServiceImpl.java`):
```java
public UserDTO updateProfileImage(String email, MultipartFile file) {
    User user = userRepository.findByEmail(email).orElseThrow(...);

    // 1. Subir imagen a Cloudinary
    Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

    // 2. Obtener URL segura (HTTPS)
    String imageUrl = (String) uploadResult.get("secure_url");

    // 3. Guardar URL en el usuario
    user.setImageUrl(imageUrl);
    userRepository.save(user);

    return mapToDTO(user);
}
```

**Flujo completo**:
```
Frontend (file input) --> POST /api/users/me/image (multipart)
    --> UserController.updateProfileImage()
    --> UserServiceImpl.updateProfileImage()
    --> Cloudinary.upload() --> devuelve URL segura
    --> Se guarda URL en BD --> Se retorna usuario con nueva imagen
```

### 4.2 Brevo - Envio de Correos Electronicos

**Archivo**: `src/main/java/com/portfolio/backend/emails/service/EmailServiceImpl.java`

Brevo (antes Sendinblue) es el servicio utilizado para enviar notificaciones por correo electronico.

**Configuracion** (en `application.yml`):
```yaml
brevo:
  api-key: ${BREVO_API_KEY:xkeysib-placeholder-key-for-dev}
  sender-email: alexchvs432@gmail.com
  sender-name: CiberPortfolio
```

**Como funciona el envio de correos**:
```java
@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Override
    @Async  // Se ejecuta en un hilo separado (no bloquea la respuesta)
    public void sendSimpleMessage(String to, String subject, String text) {
        // 1. Verifica que la API key este configurada
        if (apiKey.contains("placeholder")) {
            log.warn("API Key no configurada. Email no enviado.");
            return;
        }

        // 2. Construye el JSON para la API de Brevo
        String jsonBody = String.format(
            "{\"sender\":{\"name\":\"%s\",\"email\":\"%s\"}," +
            "\"to\":[{\"email\":\"%s\"}]," +
            "\"subject\":\"%s\",\"textContent\":\"%s\"}",
            senderName, senderEmail, to, subject, text
        );

        // 3. Envia la peticion HTTP a la API de Brevo
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.brevo.com/v3/smtp/email"))
            .header("api-key", apiKey)
            .header("content-type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
            .build();

        // 4. Ejecuta la peticion y verifica la respuesta
        HttpResponse<String> response = httpClient.send(request, ...);
    }
}
```

**Puntos clave**:
- Usa `@Async`: El envio de correos NO bloquea la respuesta al usuario. Se ejecuta en un hilo de fondo.
- Usa `java.net.http.HttpClient` nativo (no depende de librerias externas).
- API endpoint de Brevo: `https://api.brevo.com/v3/smtp/email`
- La API key se configura como variable de entorno `BREVO_API_KEY` en Render.

**Cuando se envian correos**:
1. **Al crear una asesoria**: Se notifica al programador que tiene una nueva solicitud
2. **Al cambiar estado de asesoria**: Se notifica al usuario si fue aceptada/rechazada

**Formato del correo (nueva solicitud)**:
```
Asunto: Nueva Solicitud de Asesoria - CiberPortfolio
Cuerpo:
Hola [nombre programador],
Tienes una nueva solicitud de asesoria de [nombre usuario].
Fecha: 2025-01-15
Hora: 10:00
Mensaje: Necesito ayuda con Spring Boot
Ingresa a tu dashboard para responder.
```

**Formato del correo (actualizacion de estado)**:
```
Asunto: Actualizacion de tu Asesoria - CiberPortfolio
Cuerpo:
Hola [nombre usuario],
Tu asesoria con [nombre programador] ha sido ACCEPTED.
Respuesta: Perfecto, nos vemos en el horario indicado.
```

---

## 5. Frontend - Astro

El frontend esta construido con **Astro 5.17.1** y **TypeScript**, usando una arquitectura basada en componentes.

**Tecnologias del frontend**:
| Tecnologia | Uso |
|------------|-----|
| Astro 5.17.1 | Framework SSG (Static Site Generation) |
| TypeScript | Tipado estatico |
| CSS puro | Estilos (tema oscuro con acentos neon verdes) |
| Firebase Hosting | Despliegue de la app |

**Paginas principales**:

| Pagina | Ruta | Descripcion |
|--------|------|-------------|
| Inicio | `/` | Pagina principal con listado de programadores |
| Login | `/auth/login` | Formulario de inicio de sesion |
| Registro | `/auth/register` | Formulario de registro (solo nombre, email, password) |
| Detalles | `/programmer/details` | Perfil publico del programador con proyectos |
| Dashboard Programador | `/programmer/dashboard` | Panel del programador |
| Perfil Profesional | `/programmer/profile` | Editar perfil profesional |
| Mi Cuenta | `/programmer/account` | Configuracion de cuenta |
| Solicitudes | `/programmer/requests` | Ver y gestionar asesorias recibidas |
| Dashboard Admin | `/admin/dashboard` | Panel de administracion |

---

## 6. Conexion Frontend-Backend

### 6.1 Configuracion de la API

**Archivo**: `src/lib/services/api.config.ts`

```typescript
// URL base del backend en Render
export const API_BASE_URL = import.meta.env.PUBLIC_API_URL
    || 'https://backend-spring-wgjc.onrender.com/api';
```

La URL del backend se puede configurar via variable de entorno `PUBLIC_API_URL`, pero por defecto apunta a Render.

### 6.2 Funcion de llamadas API con autenticacion

```typescript
export async function apiCall<T>(url: string, options: RequestInit = {}): Promise<T> {
    // 1. Obtiene el token JWT del localStorage
    const user = localStorage.getItem('ciber_user');
    const token = user ? JSON.parse(user).token : null;

    // 2. Agrega headers: Content-Type y Authorization (si hay token)
    const headers: HeadersInit = {
        'Content-Type': 'application/json',
        ...(token && { Authorization: `Bearer ${token}` }),
        ...options.headers,
    };

    // 3. Hace la peticion HTTP
    const response = await fetch(url, { ...options, headers });

    // 4. Si hay error, lanza excepcion
    if (!response.ok) {
        throw new Error(`API Error: ${response.statusText}`);
    }

    // 5. Retorna la respuesta como JSON
    return response.json();
}
```

### 6.3 Servicio de Autenticacion

**Archivo**: `src/lib/services/auth.service.ts`

**Login**:
```typescript
export async function login(credentials: LoginCredentials): Promise<User> {
    const response = await fetch(`${API_BASE_URL}/api/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email: credentials.email, password: credentials.password }),
    });

    const data: AuthResponse = await response.json();

    // Convierte respuesta del backend al tipo User del frontend
    const user: User = {
        id: data.id.toString(),
        name: data.name,
        email: data.email,
        role: data.role.toLowerCase() as 'user' | 'programmer' | 'admin',
        token: data.token,
    };

    setUser(user);  // Guarda en localStorage
    return user;
}
```

**Almacenamiento local**: El token JWT se guarda en `localStorage` bajo la clave `ciber_user` como un objeto JSON que incluye id, name, email, role y token.

### 6.4 Flujo completo Frontend-Backend

```
+-------------------+                    +-------------------+
|    FRONTEND       |                    |     BACKEND       |
|    (Astro)        |                    |  (Spring Boot)    |
+-------------------+                    +-------------------+
|                   |                    |                   |
| 1. Usuario llena  |   POST /api/auth   |                   |
|    formulario     | -----------------> | 2. AuthService    |
|    de login       |   {email,password} |    valida creds   |
|                   |                    |    genera JWT     |
|                   | <----------------- |                   |
|                   |   {id,name,email,  |                   |
|                   |    role,token}     |                   |
|                   |                    |                   |
| 3. Guarda token   |                    |                   |
|    en localStorage|                    |                   |
|                   |                    |                   |
| 4. Peticion con   |   GET /api/users   |                   |
|    token          | -----------------> | 5. JwtFilter      |
|                   |   Authorization:   |    valida token   |
|                   |   Bearer <token>   |    extrae email   |
|                   |                    |    carga user     |
|                   | <----------------- | 6. Controller     |
|                   |   {userData}       |    retorna data   |
+-------------------+                    +-------------------+
```

---

## 7. Despliegue y Configuracion

### 7.1 Render (Backend)

El backend se despliega en **Render.com** usando Docker.

**URL de produccion**: `https://backend-spring-wgjc.onrender.com`

#### Dockerfile (Multi-stage build)

```dockerfile
# Etapa 1: Build - Compila el proyecto con Maven
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B    # Descarga dependencias (cache de Docker)
COPY src ./src
RUN mvn clean package -DskipTests   # Compila sin ejecutar tests

# Etapa 2: Runtime - Imagen ligera solo con JRE
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN addgroup -S spring && adduser -S spring -G spring   # Usuario no-root
USER spring:spring
COPY --from=build /app/target/*.jar app.jar              # Copia JAR compilado
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=prod                          # Activa perfil produccion
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Por que Multi-stage**:
- La primera etapa usa Maven + JDK (imagen pesada ~800MB) solo para compilar
- La segunda etapa usa solo JRE Alpine (imagen ligera ~200MB) para ejecutar
- Resultado: imagen Docker mas pequena y segura

#### Variables de entorno en Render

Estas variables se configuran en el panel de Render (Environment > Environment Variables):

| Variable | Descripcion | Ejemplo |
|----------|-------------|---------|
| `DATABASE_URL` | URL de conexion a PostgreSQL | `jdbc:postgresql://host:5432/dbname` |
| `DB_USERNAME` | Usuario de la base de datos | `portfolio_user` |
| `DB_PASSWORD` | Contrasena de la base de datos | `(generada por Render)` |
| `JWT_SECRET` | Clave secreta para firmar tokens JWT | `(string larga y segura)` |
| `JWT_EXPIRATION` | Tiempo de vida del token en ms | `86400000` (24 horas) |
| `CLOUDINARY_CLOUD_NAME` | Nombre del cloud de Cloudinary | `dfarmcidt` |
| `CLOUDINARY_API_KEY` | API Key de Cloudinary | `289235364492953` |
| `CLOUDINARY_API_SECRET` | API Secret de Cloudinary | `(secreto)` |
| `BREVO_API_KEY` | API Key de Brevo para envio de emails | `xkeysib-...` |
| `SPRING_PROFILES_ACTIVE` | Perfil activo | `prod` |
| `PORT` | Puerto del servidor | `8080` |

#### Perfil de Produccion (`application-prod.yml`)

```yaml
spring:
  datasource:
    url: ${DATABASE_URL}
    driver-class-name: org.postgresql.Driver
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: update        # Actualiza esquema automaticamente
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect

cloudinary:
  cloud-name: ${CLOUDINARY_CLOUD_NAME}
  api-key: ${CLOUDINARY_API_KEY}
  api-secret: ${CLOUDINARY_API_SECRET}

jwt:
  secret: ${JWT_SECRET}
  expiration: ${JWT_EXPIRATION:86400000}
```

#### Como desplegar cambios en Render

```
1. Hacer commit de los cambios: git add . && git commit -m "descripcion"
2. Push al repositorio: git push origin main
3. Render detecta automaticamente el push
4. Construye la imagen Docker usando el Dockerfile
5. Despliega la nueva version automaticamente
```

### 7.2 Firebase Hosting (Frontend)

El frontend se despliega en **Firebase Hosting**.

**URL de produccion**: `https://backend-portfolio-f9095.web.app`
**Proyecto Firebase**: `backend-portfolio-f9095`

#### Archivo `firebase.json`

```json
{
    "hosting": {
        "public": "dist",       // Carpeta que se sube (resultado del build de Astro)
        "ignore": ["firebase.json", "**/.*", "**/node_modules/**"],
        "rewrites": [{
            "source": "**",
            "destination": "/index.html"   // SPA routing
        }],
        "headers": [{
            "source": "**/*.@(js|css|jpg|jpeg|gif|png|svg|webp|ico|woff|woff2|ttf|eot)",
            "headers": [{
                "key": "Cache-Control",
                "value": "max-age=31536000"   // Cache de 1 ano para assets estaticos
            }]
        }]
    }
}
```

#### Como desplegar cambios en Firebase

```
1. Navegar al directorio del frontend
2. Compilar: npm run build (genera la carpeta dist/)
3. Desplegar: firebase deploy --only hosting
4. Firebase sube los archivos de dist/ al hosting
5. La nueva version queda disponible en la URL de produccion
```

#### Variable de entorno del frontend

En el archivo `.env` del frontend:
```
PUBLIC_API_URL=https://backend-spring-wgjc.onrender.com
```

Esta variable se usa en `api.config.ts` y `auth.service.ts` para apuntar al backend en produccion.

---

## 8. Archivos Importantes

### Backend (Spring Boot)

| Archivo | Ubicacion | Funcion |
|---------|-----------|---------|
| `SecurityConfig.java` | `security/config/` | Configuracion de seguridad, CORS, endpoints publicos |
| `JwtUtil.java` | `security/utils/` | Generacion y validacion de tokens JWT |
| `JwtAuthenticationFilter.java` | `security/filters/` | Filtro que intercepta cada request y valida JWT |
| `AuthService.java` | `security/services/` | Logica de login y registro |
| `User.java` | `users/entity/` | Entidad principal con relaciones cascade |
| `UserServiceImpl.java` | `users/services/` | Logica de gestion de usuarios y subida de imagenes |
| `ProgrammerProfile.java` | `profiles/entity/` | Entidad del perfil profesional |
| `Project.java` | `projects/entity/` | Entidad de proyectos del portafolio |
| `Schedule.java` | `schedules/entity/` | Entidad de horarios con auto-calculo |
| `Advisory.java` | `advisories/entity/` | Entidad de asesorias |
| `AdvisoryServiceImpl.java` | `advisories/services/` | Logica de asesorias + envio de emails |
| `CloudinaryConfig.java` | `config/` | Configuracion del bean Cloudinary |
| `EmailServiceImpl.java` | `emails/service/` | Envio de correos via Brevo API |
| `application.yml` | `resources/` | Configuracion base (JWT, Brevo, puerto) |
| `application-prod.yml` | `resources/` | Configuracion de produccion (PostgreSQL, Cloudinary) |
| `Dockerfile` | `raiz/` | Build multi-stage para despliegue en Render |
| `pom.xml` | `raiz/` | Dependencias Maven del proyecto |

### Frontend (Astro)

| Archivo | Ubicacion | Funcion |
|---------|-----------|---------|
| `api.config.ts` | `src/lib/services/` | URL base del backend y endpoints API |
| `auth.service.ts` | `src/lib/services/` | Funciones de login, register, logout |
| `user.types.ts` | `src/types/` | Tipos TypeScript para User, LoginCredentials, RegisterData |
| `Layout.astro` | `src/layouts/` | Layout principal con SEO (Open Graph, Twitter Cards) |
| `firebase.json` | `raiz/` | Configuracion de Firebase Hosting |
| `.env` | `raiz/` | Variable PUBLIC_API_URL del backend |

---

*Informe generado para el proyecto CiberPortfolio - Plataforma de portafolio y asesorias para programadores.*
