package com.portfolio.backend.projects.dtos;

import com.portfolio.backend.projects.entity.enums.ProjectType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO para actualizar un proyecto existente
 * 
 * Todos los campos son OPCIONALES para permitir actualizaciones parciales.
 * Solo se validarán los campos que se envíen.
 * 
 * Ejemplo: Si solo se envía "name", solo se actualizará el nombre.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProjectRequest {

    /**
     * Nombre del proyecto
     * Validaciones:
     * - Opcional (null significa "no actualizar")
     * - Si se envía: 3-100 caracteres
     */
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String name;

    /**
     * Descripción del proyecto
     * Validaciones:
     * - Opcional
     * - Si se envía: máximo 1000 caracteres
     */
    @Size(max = 1000, message = "La descripción no puede superar 1000 caracteres")
    private String description;

    /**
     * Tipo de proyecto
     * Validaciones:
     * - Opcional
     * - Si se envía: debe ser válido (WEB, MOBILE, etc.)
     */
    private ProjectType type;

    /**
     * Lista de tecnologías
     * Validaciones:
     * - Opcional
     * - Si se envía: máximo 30, no vacías
     */
    @Size(max = 30, message = "Máximo 30 tecnologías permitidas")
    private List<@NotBlank(message = "Las tecnologías no pueden estar vacías") String> techs;

    /**
     * URL de la imagen
     * Validaciones:
     * - Opcional
     * - Si se envía: debe ser URL válida
     */
    @Pattern(regexp = "^https?://.*", message = "La URL de la imagen debe comenzar con http:// o https://")
    private String imageUrl;

    /**
     * URL del repositorio
     * Validaciones:
     * - Opcional
     * - Si se envía: debe ser URL de GitHub válida
     */
    @Pattern(regexp = "^https://github\\.com/[a-zA-Z0-9_-]+/[a-zA-Z0-9_-]+/?$", message = "La URL del repositorio debe tener el formato: https://github.com/usuario/proyecto")
    private String repoUrl;

    /**
     * URL de deploy
     * Validaciones:
     * - Opcional
     * - Si se envía: debe ser URL válida
     */
    @Pattern(regexp = "^https?://.*", message = "La URL de deploy debe comenzar con http:// o https://")
    private String deployUrl;
}
