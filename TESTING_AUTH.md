# 🚀 Cómo Probar Login y Registro

## ✅ Estado Actual

**Backend Spring Boot** ✅ Corriendo en `http://localhost:8080`
**Frontend Astro** ✅ Corriendo en `http://localhost:4321`

Los dos servicios ya están **conectados** y listos para probar.

---

## 🧪 Pasos para Probar

### 1. **Registro de Usuario**

1. Abre tu navegador en: `http://localhost:4321/auth/register`
2. Llena el formulario:
   - **Nombre**: Tu nombre
   - **Email**: cualquier@ejemplo.com
   - **Contraseña**: mínimo 6 caracteres
   - **Confirmar**: misma contraseña
3. Clic en **"INICIAR REGISTRO"**
4. Si todo está bien:
   - ✅ Se crea el usuario en la base de datos H2
   - ✅ Recibes un token JWT
   - ✅ Te redirige automáticamente a `/` (o dashboard si eres programmer/admin)

**Errores comunes:**
- ❌ "El correo ya está registrado" → Usa otro email
- ❌ "Error de conexión" → Verifica que Spring Boot esté corriendo

---

### 2. **Login**

1. Abre: `http://localhost:4321/auth/login`
2. Ingresa el email y contraseña del usuario que registraste
3. Clic en **"INICIAR SESIÓN"**
4. Si todo está bien:
   - ✅ Recibes el token JWT
   - ✅ Te redirige según tu rol:
     - **ADMIN** → `/admin/dashboard`
     - **PROGRAMMER** → `/programmer/dashboard`
     - **USER** → `/`

---

### 3. **Ver en la Base de Datos (Opcional)**

Si quieres ver los usuarios creados:

1. Abre: `http://localhost:8080/h2-console`
2. **JDBC URL**: `jdbc:h2:mem:portfolio_db`
3. **User**: `sa`
4. **Password**: *(vacío)*
5. Clic en **Connect**
6. Ejecuta:
   ```sql
   SELECT * FROM USERS;
   ```

Verás todos los usuarios registrados con sus tokens JWT.

---

## 🔧 Troubleshooting

### Error: "Se denegó el acceso a localhost"

➡️ El backend NO está corriendo.

**Solución:**
```bash
cd c:\Users\Usuario\Desktop\U\Proyecto-Backend\project-backend\backend-spring
.\mvnw.cmd spring-boot:run
```

---

### Error: "Error de conexión con el servidor"

➡️ El frontend no puede conectarse al backend.

**Verificar:**
1. Backend corriendo en puerto **8080**
2. Frontend corriendo en puerto **4321**
3. CORS configurado correctamente (ya lo está)

---

### Error: "Credenciales inválidas"

➡️ Password incorrecto.

**Nota:** La contraseña se encripta con BCrypt. Asegúrate de usar la contraseña exacta que pusiste al registrarte.

---

## 📊 Flujo Completo

```
Usuario rellena formulario
    ↓
JavaScript (Astro) hace fetch()
    ↓
POST http://localhost:8080/api/auth/register
    ↓
Spring Boot valida y crea usuario
    ↓
Devuelve: { id, name, email, role, token }
    ↓
Frontend guarda en localStorage
    ↓
Redirige a dashboard
```

---

## 🎯 Qué Probar

1. ✅ Registro con datos válidos
2. ✅ Registro con email duplicado (debe fallar)
3. ✅ Registro con contraseña corta (debe fallar)
4. ✅ Login con credenciales correctas
5. ✅ Login con credenciales incorrectas (debe fallar)
6. ✅ Ver que el token se guarda en localStorage
   - Abre DevTools → Application → Local Storage → `localhost:4321`
   - Busca la key `ciber_user`

---

## ✨ Lo que YA funciona

- ✅ Registro de usuarios
- ✅ Login de usuarios
- ✅ Generación de tokens JWT
- ✅ Encriptación de passwords (BCrypt)
- ✅ Validación de datos
- ✅ Manejo de errores
- ✅ Redirección por roles

---

## 🚧 Lo que FALTA

- ❌ Endpoints de proyectos (`/api/projects`)
- ❌ Endpoints de asesorías (`/api/advisories`)
- ❌ Dashboard con datos reales
- ❌ Protección de rutas en frontend
- ❌ Renovación de tokens (refresh token)
