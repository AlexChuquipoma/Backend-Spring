package com.portfolio.backend.projects.dtos;

import com.portfolio.backend.projects.entity.enums.ProjectType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO para crear un nuevo proyecto
 * 
 * Incluye validaciones Jakarta Validation para garantizar
 * que los datos recibidos cumplan con los requisitos de negocio.
 * 
 * Validaciones aplicadas:
 * - Nombre: Obligatorio, 3-100 caracteres
 * - Descripción: Opcional, máximo 1000 caracteres
 * - Tipo: Obligatorio (WEB, MOBILE, DESKTOP, etc.)
 * - Tecnologías: Opcional, máximo 30, no vacías
 * - URLs: Opcional pero deben tener formato válido
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProjectRequest {

    /**
     * Nombre del proyecto
     * Validaciones:
     * - No puede estar vacío
     * - Longitud: 3-100 caracteres
     */
    @NotBlank(message = "El nombre del proyecto es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String name;

    /**
     * Descripción del proyecto
     * Validaciones:
     * - Opcional
     * - Máximo 1000 caracteres si se proporciona
     */
    @Size(max = 1000, message = "La descripción no puede superar 1000 caracteres")
    private String description;

    /**
     * Tipo de proyecto
     * Validaciones:
     * - Obligatorio
     * - Debe ser uno de: WEB, MOBILE, DESKTOP, etc.
     */
    @NotNull(message = "El tipo de proyecto es obligatorio")
    private ProjectType type;

    /**
     * Lista de tecnologías utilizadas
     * Validaciones:
     * - Opcional
     * - Máximo 30 tecnologías
     * - Cada tecnología no puede estar vacía
     */
    @Size(max = 30, message = "Máximo 30 tecnologías permitidas")
    private List<@NotBlank(message = "Las tecnologías no pueden estar vacías") String> techs;

    /**
     * URL de la imagen del proyecto
     * Validaciones:
     * - Opcional
     * - Debe ser una URL válida (http o https)
     */
    @Pattern(regexp = "^https?://.*", message = "La URL de la imagen debe comenzar con http:// o https://")
    private String imageUrl;

    /**
     * URL del repositorio en GitHub
     * Validaciones:
     * - Opcional
     * - Debe ser una URL de GitHub válida
     */
    @Pattern(regexp = "^https://github\\.com/.+/.+$", message = "La URL del repositorio debe tener el formato: https://github.com/usuario/proyecto")
    private String repoUrl;

    /**
     * URL del proyecto desplegado
     * Validaciones:
     * - Opcional
     * - Debe ser una URL válida (http o https)
     */
    @Pattern(regexp = "^https?://.*", message = "La URL de deploy debe comenzar con http:// o https://")
    private String deployUrl;
}
