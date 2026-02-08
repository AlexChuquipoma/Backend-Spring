package com.portfolio.backend.projects.services;

import com.portfolio.backend.projects.dtos.CreateProjectRequest;
import com.portfolio.backend.projects.dtos.ProjectDTO;
import com.portfolio.backend.projects.dtos.UpdateProjectRequest;
import com.portfolio.backend.projects.entity.Project;
import com.portfolio.backend.projects.repository.ProjectRepository;
import com.portfolio.backend.users.entity.User;
import com.portfolio.backend.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de Projects
 * 
 * Contiene la lógica de negocio para gestionar proyectos:
 * - Validar ownership (solo el dueño puede editar/eliminar)
 * - Convertir entre entidades y DTOs
 * - Operaciones transaccionales
 * 
 * @Service: Marca esta clase como un servicio de Spring
 * @RequiredArgsConstructor: Inyección de dependencias por constructor (Lombok)
 */
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    /**
     * Crear un nuevo proyecto
     * 
     * Flujo:
     * 1. Buscar usuario por email
     * 2. Crear entidad Project
     * 3. Guardar en BD
     * 4. Convertir a DTO y retornar
     * 
     * @Transactional: Garantiza atomicidad de la operación
     */
    @Override
    @Transactional
    public ProjectDTO createProject(String userEmail, CreateProjectRequest request) {
        // 1. Buscar usuario
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Crear proyecto
        Project project = Project.builder()
                .name(request.getName())
                .description(request.getDescription())
                .type(request.getType())
                .techs(request.getTechs())
                .imageUrl(request.getImageUrl())
                .repoUrl(request.getRepoUrl())
                .deployUrl(request.getDeployUrl())
                .owner(user)
                .build();

        // 3. Guardar en BD
        Project savedProject = projectRepository.save(project);

        // 4. Convertir a DTO
        return convertToDTO(savedProject);
    }

    /**
     * Actualizar proyecto existente
     * 
     * Validaciones:
     * - Proyecto debe existir
     * - Usuario debe ser el dueño del proyecto
     * 
     * Solo actualiza campos NO nulos (actualización parcial)
     */
    @Override
    @Transactional
    public ProjectDTO updateProject(Long projectId, String userEmail, UpdateProjectRequest request) {
        // 1. Buscar proyecto
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // 2. Validar ownership
        if (!project.getOwner().getEmail().equals(userEmail)) {
            throw new RuntimeException("You don't have permission to edit this project");
        }

        // 3. Actualizar campos (solo los que se envíen)
        if (request.getName() != null) {
            project.setName(request.getName());
        }
        if (request.getDescription() != null) {
            project.setDescription(request.getDescription());
        }
        if (request.getType() != null) {
            project.setType(request.getType());
        }
        if (request.getTechs() != null) {
            project.setTechs(request.getTechs());
        }
        if (request.getImageUrl() != null) {
            project.setImageUrl(request.getImageUrl());
        }
        if (request.getRepoUrl() != null) {
            project.setRepoUrl(request.getRepoUrl());
        }
        if (request.getDeployUrl() != null) {
            project.setDeployUrl(request.getDeployUrl());
        }

        // 4. Guardar cambios
        Project updatedProject = projectRepository.save(project);

        // 5. Retornar DTO
        return convertToDTO(updatedProject);
    }

    /**
     * Eliminar proyecto
     * 
     * Validaciones:
     * - Proyecto debe existir
     * - Usuario debe ser el dueño
     */
    @Override
    @Transactional
    public void deleteProject(Long projectId, String userEmail) {
        // 1. Buscar proyecto
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // 2. Validar ownership
        if (!project.getOwner().getEmail().equals(userEmail)) {
            throw new RuntimeException("You don't have permission to delete this project");
        }

        // 3. Eliminar
        projectRepository.delete(project);
    }

    /**
     * Obtener proyecto por ID (público)
     */
    @Override
    public ProjectDTO getProjectById(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        return convertToDTO(project);
    }

    /**
     * Obtener proyectos del usuario autenticado
     */
    @Override
    public List<ProjectDTO> getMyProjects(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Project> projects = projectRepository.findByOwner(user);

        return projects.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener proyectos de un usuario por ID (público)
     */
    @Override
    public List<ProjectDTO> getProjectsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Project> projects = projectRepository.findByOwner(user);

        return projects.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener TODOS los proyectos (público)
     */
    @Override
    public List<ProjectDTO> getAllProjects() {
        List<Project> projects = projectRepository.findAll();

        return projects.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convertir entidad Project a DTO
     * 
     * Mapea todos los campos incluyendo información del dueño (owner).
     * Separa la capa de persistencia de la capa de presentación.
     */
    private ProjectDTO convertToDTO(Project project) {
        return ProjectDTO.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .type(project.getType())
                .techs(project.getTechs())
                .imageUrl(project.getImageUrl())
                .repoUrl(project.getRepoUrl())
                .deployUrl(project.getDeployUrl())
                .ownerId(project.getOwner().getId())
                .ownerName(project.getOwner().getName())
                .ownerEmail(project.getOwner().getEmail())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }
}
