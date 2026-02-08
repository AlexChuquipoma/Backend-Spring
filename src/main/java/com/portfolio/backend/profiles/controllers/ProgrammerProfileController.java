package com.portfolio.backend.profiles.controllers;

import com.portfolio.backend.profiles.dtos.ProgrammerProfileDTO;
import com.portfolio.backend.profiles.dtos.UpdateProfileRequest;
import com.portfolio.backend.profiles.services.ProgrammerProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para gestionar perfiles de programadores.
 *
 * Endpoints disponibles:
 * - GET /api/profiles/me - Obtener MI perfil (requiere auth)
 * - GET /api/profiles/user/{userId} - Ver perfil público (sin auth)
 * - POST /api/profiles - Crear/actualizar perfil (requiere auth)
 * - PUT /api/profiles - Actualizar perfil (requiere auth)
 * - DELETE /api/profiles - Eliminar perfil (requiere auth)
 *
 * @RestController: Combina @Controller + @ResponseBody (respuestas automáticas
 *                  en JSON)
 * @RequestMapping: Prefijo base para todos los endpoints (/api/profiles)
 * @RequiredArgsConstructor: Inyección de dependencias por constructor
 * @CrossOrigin: Permite peticiones desde cualquier origen (necesario para
 *               frontend)
 */
@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProgrammerProfileController {

    private final ProgrammerProfileService profileService;

    /**
     * Obtiene el perfil del usuario autenticado actual.
     *
     * Endpoint: GET /api/profiles/me
     * Requiere: Token JWT en header Authorization
     *
     * ¿Cómo funciona?
     * 1. Spring Security valida el token JWT
     * 2. Extrae el email del usuario del token
     * 3. Lo pasa como parámetro 'Authentication'
     * 4. authentication.getName() retorna el email del usuario
     *
     * @param authentication Objeto con info del usuario autenticado (inyectado por
     *                       Spring Security)
     * @return ResponseEntity con el perfil del usuario
     */
    @GetMapping("/me")
    public ResponseEntity<ProgrammerProfileDTO> getMyProfile(Authentication authentication) {
        // Obtener email del usuario desde el token JWT
        String userEmail = authentication.getName();

        // Llamar al servicio para obtener el perfil
        ProgrammerProfileDTO profile = profileService.getMyProfile(userEmail);

        // Retornar respuesta HTTP 200 OK con el perfil en JSON
        return ResponseEntity.ok(profile);
    }

    /**
     * Obtiene el perfil público de un programador por su ID de usuario.
     *
     * Endpoint: GET /api/profiles/user/{userId}
     * Acceso: PÚBLICO (no requiere autenticación)
     *
     * Ejemplo: GET /api/profiles/user/5
     *
     * @param userId ID del usuario cuyo perfil se quiere ver
     * @return ResponseEntity con el perfil público
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<ProgrammerProfileDTO> getProfileByUserId(@PathVariable Long userId) {
        ProgrammerProfileDTO profile = profileService.getProfileByUserId(userId);
        return ResponseEntity.ok(profile);
    }

    /**
     * Crea o actualiza el perfil del usuario autenticado.
     *
     * Endpoint: POST /api/profiles
     * Requiere: Token JWT + rol PROGRAMMER
     * Body: JSON con los datos del perfil
     *
     * Ejemplo de body:
     * {
     * "jobTitle": "Full Stack Developer",
     * "bio": "Passionate developer...",
     * "skills": ["Java", "Spring Boot", "React"],
     * "githubUrl": "https://github.com/usuario",
     * "yearsExperience": 5
     * }
     *
     * @param request        Datos del perfil (deserializado automáticamente desde
     *                       JSON)
     * @param authentication Info del usuario autenticado
     * @return ResponseEntity con el perfil creado/actualizado
     */
    @PostMapping
    public ResponseEntity<ProgrammerProfileDTO> createOrUpdateProfile(
            @RequestBody UpdateProfileRequest request,
            Authentication authentication) {
        String userEmail = authentication.getName();
        ProgrammerProfileDTO profile = profileService.createOrUpdateProfile(userEmail, request);
        return ResponseEntity.ok(profile);
    }

    /**
     * Actualiza el perfil (alias de POST).
     *
     * Endpoint: PUT /api/profiles
     * Funcionalidad: Idéntica a POST
     *
     * ¿Por qué tener POST y PUT?
     * - Semántica REST: PUT es más apropiado para actualizaciones
     * - Compatibilidad: Algunos clientes prefieren usar PUT para updates
     * - Ambos hacen lo mismo internamente (create or update)
     */
    @PutMapping
    public ResponseEntity<ProgrammerProfileDTO> updateProfile(
            @RequestBody UpdateProfileRequest request,
            Authentication authentication) {
        String userEmail = authentication.getName();
        ProgrammerProfileDTO profile = profileService.createOrUpdateProfile(userEmail, request);
        return ResponseEntity.ok(profile);
    }

    /**
     * Elimina el perfil del usuario autenticado.
     *
     * Endpoint: DELETE /api/profiles
     * Requiere: Token JWT
     *
     * Respuesta: 204 No Content (éxito sin cuerpo de respuesta)
     *
     * @param authentication Info del usuario autenticado
     * @return ResponseEntity vacío con status 204
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteProfile(Authentication authentication) {
        String userEmail = authentication.getName();
        profileService.deleteProfile(userEmail);

        // 204 No Content: Operación exitosa, sin contenido en la respuesta
        return ResponseEntity.noContent().build();
    }
}
