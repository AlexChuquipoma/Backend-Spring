package com.portfolio.backend.projects.controllers;

import com.portfolio.backend.projects.dtos.CreateProjectRequest;
import com.portfolio.backend.projects.dtos.ProjectDTO;
import com.portfolio.backend.projects.dtos.UpdateProjectRequest;
import com.portfolio.backend.projects.services.ProjectService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar proyectos
 *
 * Endpoints disponibles:
 * - POST /api/projects - Crear proyecto (requiere auth)
 * - PUT /api/projects/{id} - Actualizar proyecto (requiere auth + ownership)
 * - DELETE /api/projects/{id} - Eliminar proyecto (requiere auth + ownership)
 * - GET /api/projects/{id} - Ver proyecto (público)
 * - GET /api/projects/me - Mis proyectos (requiere auth)
 * - GET /api/projects/user/{userId} - Proyectos de usuario (público)
 * - GET /api/projects - Listar todos (público)
 *
 * @RestController: Combina @Controller + @ResponseBody
 * @RequestMapping: Prefijo base para todos los endpoints
 * @RequiredArgsConstructor: Inyección de dependencias por constructor
 * @CrossOrigin: Permite peticiones desde cualquier origen (frontend)
 */
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Proyectos", description = "CRUD de proyectos del portafolio")
public class ProjectController {

    private final ProjectService projectService;

    /**
     * Crear un nuevo proyecto
     * 
     * Endpoint: POST /api/projects
     * Requiere: Token JWT en header Authorization
     * Body: JSON con datos del proyecto
     * 
     * Ejemplo de body:
     * {
     * "name": "Portfolio Personal",
     * "description": "Mi portafolio web...",
     * "type": "WEB",
     * "techs": ["React", "Spring Boot", "PostgreSQL"],
     * "repoUrl": "https://github.com/usuario/portfolio",
     * "deployUrl": "https://miportfolio.com"
     * }
     * 
     * @param request        Datos del proyecto (validado con @Valid)
     * @param authentication Info del usuario autenticado (inyectado por Spring
     *                       Security)
     * @return 201 Created con el proyecto creado
     */
    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(
            @Valid @RequestBody CreateProjectRequest request,
            Authentication authentication) {

        // Obtener email del usuario desde el token JWT
        String userEmail = authentication.getName();

        // Crear proyecto
        ProjectDTO project = projectService.createProject(userEmail, request);

        // Retornar 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(project);
    }

    /**
     * Actualizar un proyecto existente
     * 
     * Endpoint: PUT /api/projects/{id}
     * Requiere: Token JWT + ser el dueño del proyecto
     * Body: JSON con campos a actualizar (parcial)
     * 
     * Solo el dueño del proyecto puede actualizarlo.
     * 
     * @param id             ID del proyecto a actualizar
     * @param request        Campos a actualizar (validado con @Valid)
     * @param authentication Info del usuario autenticado
     * @return 200 OK con el proyecto actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProjectRequest request,
            Authentication authentication) {

        String userEmail = authentication.getName();

        ProjectDTO project = projectService.updateProject(id, userEmail, request);

        return ResponseEntity.ok(project);
    }

    /**
     * Eliminar un proyecto
     * 
     * Endpoint: DELETE /api/projects/{id}
     * Requiere: Token JWT + ser el dueño del proyecto
     * 
     * Solo el dueño puede eliminar el proyecto.
     * 
     * @param id             ID del proyecto a eliminar
     * @param authentication Info del usuario autenticado
     * @return 204 No Content (éxito sin cuerpo de respuesta)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(
            @PathVariable Long id,
            Authentication authentication) {

        String userEmail = authentication.getName();

        projectService.deleteProject(id, userEmail);

        // 204 No Content: Operación exitosa, sin contenido
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtener un proyecto por ID
     * 
     * Endpoint: GET /api/projects/{id}
     * Acceso: PÚBLICO (no requiere autenticación)
     * 
     * Útil para ver detalles de un proyecto específico.
     * 
     * @param id ID del proyecto
     * @return 200 OK con el proyecto
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id) {
        ProjectDTO project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

    /**
     * Obtener MIS proyectos (del usuario autenticado)
     * 
     * Endpoint: GET /api/projects/me
     * Requiere: Token JWT
     * 
     * Retorna todos los proyectos creados por el usuario autenticado.
     * 
     * @param authentication Info del usuario autenticado
     * @return 200 OK con lista de proyectos
     */
    @GetMapping("/me")
    public ResponseEntity<List<ProjectDTO>> getMyProjects(Authentication authentication) {
        String userEmail = authentication.getName();

        List<ProjectDTO> projects = projectService.getMyProjects(userEmail);

        return ResponseEntity.ok(projects);
    }

    /**
     * Obtener proyectos de un usuario específico
     * 
     * Endpoint: GET /api/projects/user/{userId}
     * Acceso: PÚBLICO (no requiere autenticación)
     * 
     * Útil para ver el portfolio de otros usuarios/programadores.
     * 
     * @param userId ID del usuario
     * @return 200 OK con lista de proyectos del usuario
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ProjectDTO>> getProjectsByUserId(@PathVariable Long userId) {
        List<ProjectDTO> projects = projectService.getProjectsByUserId(userId);
        return ResponseEntity.ok(projects);
    }

    /**
     * Obtener TODOS los proyectos
     * 
     * Endpoint: GET /api/projects
     * Acceso: PÚBLICO (no requiere autenticación)
     * 
     * Útil para:
     * - Página principal mostrando todos los proyectos
     * - Explorar proyectos de la comunidad
     * - Buscar inspiración
     * 
     * @return 200 OK con lista de todos los proyectos
     */
    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        List<ProjectDTO> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }
}
