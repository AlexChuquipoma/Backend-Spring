package com.portfolio.backend.projects.dtos;

import com.portfolio.backend.projects.entity.enums.ProjectType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de respuesta para Project
 * 
 * Se usa para retornar información de proyectos al frontend.
 * Incluye datos del proyecto y del dueño (owner).
 * 
 * Este DTO NO expone entidades JPA directamente, separando
 * la capa de presentación de la capa de persistencia.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {

    /**
     * ID único del proyecto
     */
    private Long id;

    /**
     * Nombre del proyecto
     */
    private String name;

    /**
     * Descripción detallada del proyecto
     */
    private String description;

    /**
     * Tipo de proyecto (WEB, MOBILE, DESKTOP, etc.)
     */
    private ProjectType type;

    /**
     * Lista de tecnologías utilizadas
     * Ejemplo: ["Java", "Spring Boot", "React", "PostgreSQL"]
     */
    private List<String> techs;

    /**
     * URL de la imagen/preview del proyecto
     */
    private String imageUrl;

    /**
     * URL del repositorio en GitHub
     */
    private String repoUrl;

    /**
     * URL del proyecto desplegado (producción)
     */
    private String deployUrl;

    /**
     * ID del dueño del proyecto (User)
     */
    private Long ownerId;

    /**
     * Nombre del dueño del proyecto
     */
    private String ownerName;

    /**
     * Email del dueño del proyecto
     */
    private String ownerEmail;

    /**
     * Fecha de creación del proyecto
     */
    private LocalDateTime createdAt;

    /**
     * Fecha de última actualización
     */
    private LocalDateTime updatedAt;
}
