package com.portfolio.backend.projects.services;

import com.portfolio.backend.projects.dtos.CreateProjectRequest;
import com.portfolio.backend.projects.dtos.ProjectDTO;
import com.portfolio.backend.projects.dtos.UpdateProjectRequest;

import java.util.List;

/**
 * Interface para el servicio de Projects
 * 
 * Define las operaciones de negocio disponibles para gestionar proyectos.
 * La implementación está en ProjectServiceImpl.
 */
public interface ProjectService {

    /**
     * Crear un nuevo proyecto
     * 
     * @param userEmail Email del usuario autenticado (del JWT)
     * @param request   Datos del proyecto a crear
     * @return DTO del proyecto creado
     * @throws RuntimeException si el usuario no existe
     */
    ProjectDTO createProject(String userEmail, CreateProjectRequest request);

    /**
     * Actualizar un proyecto existente
     * 
     * Solo el dueño del proyecto puede actualizarlo.
     * 
     * @param projectId ID del proyecto a actualizar
     * @param userEmail Email del usuario autenticado (del JWT)
     * @param request   Campos a actualizar (parcial)
     * @return DTO del proyecto actualizado
     * @throws RuntimeException si el proyecto no existe o el usuario no es el dueño
     */
    ProjectDTO updateProject(Long projectId, String userEmail, UpdateProjectRequest request);

    /**
     * Eliminar un proyecto
     * 
     * Solo el dueño del proyecto puede eliminarlo.
     * 
     * @param projectId ID del proyecto a eliminar
     * @param userEmail Email del usuario autenticado (del JWT)
     * @throws RuntimeException si el proyecto no existe o el usuario no es el dueño
     */
    void deleteProject(Long projectId, String userEmail);

    /**
     * Obtener un proyecto por ID (público)
     * 
     * @param projectId ID del proyecto
     * @return DTO del proyecto
     * @throws RuntimeException si el proyecto no existe
     */
    ProjectDTO getProjectById(Long projectId);

    /**
     * Obtener todos los proyectos del usuario autenticado
     * 
     * @param userEmail Email del usuario autenticado (del JWT)
     * @return Lista de proyectos del usuario
     */
    List<ProjectDTO> getMyProjects(String userEmail);

    /**
     * Obtener todos los proyectos de un usuario por ID (público)
     * 
     * Útil para ver el portfolio de otros usuarios.
     * 
     * @param userId ID del usuario
     * @return Lista de proyectos del usuario
     * @throws RuntimeException si el usuario no existe
     */
    List<ProjectDTO> getProjectsByUserId(Long userId);

    /**
     * Obtener TODOS los proyectos (público)
     * 
     * Útil para página principal o explorar proyectos.
     * 
     * @return Lista de todos los proyectos
     */
    List<ProjectDTO> getAllProjects();
}
