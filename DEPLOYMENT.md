# 🚀 Guía de Despliegue en Render

Esta guía te llevará paso a paso para desplegar tu backend Spring Boot en Render usando Docker y PostgreSQL.

---

## 📋 Prerrequisitos

- ✅ Cuenta en [Render.com](https://render.com) (crear una gratis)
- ✅ Repositorio Git (GitHub, GitLab, o Bitbucket) con tu código
- ✅ Docker configurado (ya está en tu proyecto)

---

## 🗄️ Paso 1: Crear Base de Datos PostgreSQL

1. **Inicia sesión en Render**
2. Haz clic en **"New +"** → **"PostgreSQL"**
3. Configura la base de datos:
   - **Name**: `portfolio-db` (o el nombre que prefieras)
   - **Database**: `portfolio_db`
   - **User**: se genera automáticamente
   - **Region**: Selecciona la más cercana (ej: Ohio, US East)
   - **Instance Type**: **Free**
4. Haz clic en **"Create Database"**
5. **Guarda los datos de conexión** (los necesitarás después):
   - Internal Database URL
   - External Database URL
   - PSQL Command

---

## 🐳 Paso 2: Preparar Repositorio Git

### Opción A: Si ya tienes el proyecto en GitHub/GitLab
✅ Solo asegúrate de que el `Dockerfile` esté en la raíz del proyecto

### Opción B: Si no tienes repositorio remoto

```bash
# Inicializar Git (si no está inicializado)
cd c:\Users\Usuario\Desktop\U\Proyecto-Backend\project-backend\backend-spring
git init

# Agregar archivos
git add .
git commit -m "Setup Docker and database profiles"

# Crear repositorio en GitHub y conectarlo
git remote add origin https://github.com/TU_USUARIO/TU_REPO.git
git branch -M main
git push -u origin main
```

---

## 🌐 Paso 3: Crear Web Service en Render

1. En Render, haz clic en **"New +"** → **"Web Service"**
2. Conecta tu repositorio:
   - Selecciona **GitHub** (o GitLab/Bitbucket)
   - Autoriza a Render
   - Selecciona tu repositorio
3. Configura el servicio:
   - **Name**: `portfolio-backend` (o el que prefieras)
   - **Region**: La misma que la base de datos
   - **Branch**: `main` (o la rama que uses)
   - **Root Directory**: `backend-spring` (si el Dockerfile no está en la raíz)
   - **Environment**: **Docker**
   - **Instance Type**: **Free**
4. Ve a la pestaña **"Environment Variables"** (abajo)

---

## 🔐 Paso 4: Configurar Variables de Entorno

Agrega las siguientes variables de entorno en Render:

| Key | Value | Descripción |
|-----|-------|-------------|
| `SPRING_PROFILES_ACTIVE` | `prod` | Activa el perfil de producción |
| `DATABASE_URL` | *Copiar de PostgreSQL* | URL interna de la BD |
| `JWT_SECRET` | *Generar clave segura* | Clave para JWT (min. 256 bits) |
| `JWT_EXPIRATION` | `86400000` | Expiración en ms (24h) |
| `PORT` | `8080` | Puerto del servidor |

### 🔑 Generar JWT_SECRET seguro

```bash
# En PowerShell (puedes usar este comando)
-join ((48..57) + (65..90) + (97..122) | Get-Random -Count 64 | % {[char]$_})
```

O usa un generador online: [RandomKeygen](https://randomkeygen.com/)

---

## 🚀 Paso 5: Desplegar

1. Haz clic en **"Create Web Service"**
2. Render comenzará a:
   - Clonar tu repositorio
   - Construir la imagen Docker
   - Desplegar el contenedor
3. **Espera 5-10 minutos** (primer deploy es más lento)
4. Una vez completado, verás:
   - ✅ **Estado**: "Live"
   - 🔗 **URL pública**: `https://portfolio-backend-xxxx.onrender.com`

---

## ✅ Paso 6: Verificar Despliegue

### Prueba los endpoints

```bash
# Health check (si tienes uno)
curl https://TU-URL.onrender.com/actuator/health

# Test de autenticación
curl -X POST https://TU-URL.onrender.com/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "password": "password123",
    "role": "USER"
  }'
```

### Ver logs en Render
- Ve a tu Web Service en Render
- Haz clic en la pestaña **"Logs"**
- Verifica que no haya errores

---

## 🐛 Solución de Problemas Comunes

### ❌ Error: "Container failed to start"

**Causa**: Variables de entorno mal configuradas

**Solución**:
1. Revisa las variables en Render
2. Asegúrate de que `DATABASE_URL` sea la URL **interna**
3. Verifica que `JWT_SECRET` tenga al menos 256 bits
4. Redeploy: **Manual Deploy** → **"Clear build cache & deploy"**

### ❌ Error: "Connection to database refused"

**Causa**: Base de datos no está vinculada

**Solución**:
1. En tu Web Service, ve a **"Environment"**
2. Agrega la variable `DATABASE_URL` con la URL interna de PostgreSQL
3. Asegúrate de que la BD esté en la misma región

### ❌ Error 503 / Timeout

**Causa**: La app tarda mucho en iniciar

**Solución**:
1. El plan gratuito de Render hiberna después de 15 min de inactividad
2. La primera petición puede tardar 30-60 segundos
3. Considera agregar un health check endpoint

---

## 🔄 Redesplegar Cambios

Cada vez que hagas `git push` a tu rama principal, Render automáticamente:
1. Detecta los cambios
2. Reconstruye la imagen Docker
3. Redespliega la aplicación

**Despliegue manual**:
- Ve a tu Web Service → **"Manual Deploy"** → **"Deploy latest commit"**

---

## 📊 Configurar Frontend (Firebase Hosting)

Una vez que tu backend esté en vivo, actualiza tu frontend:

```typescript
// En tu frontend (Astro/Angular)
const API_URL = 'https://portfolio-backend-xxxx.onrender.com';

// Ejemplo de petición
fetch(`${API_URL}/api/auth/login`, {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ email, password })
})
```

### Configurar CORS

Ya está configurado en `SecurityConfig.java` para permitir Firebase Hosting.

---

## 🎯 Checklist de Despliegue

- [ ] Base de datos PostgreSQL creada en Render
- [ ] Repositorio Git con código actualizado
- [ ] Web Service creado en Render
- [ ] Variables de entorno configuradas
- [ ] Primer despliegue exitoso (estado "Live")
- [ ] Endpoints de autenticación probados
- [ ] Frontend actualizado con URL del backend
- [ ] CORS configurado correctamente
- [ ] Logs revisados sin errores
- [ ] Documentación de API lista (Swagger después)

---

## 📚 Recursos Adicionales

- [Documentación oficial de Render](https://render.com/docs)
- [Guía de Docker](https://docs.docker.com/)
- [Spring Boot en producción](https://spring.io/guides/gs/spring-boot-docker/)

---

## 🆘 ¿Problemas?

Si encuentras errores:
1. Revisa los logs en Render
2. Verifica las variables de entorno
3. Prueba el Docker localmente: `docker-compose up`
4. Consulta con el profesor o compañeros
