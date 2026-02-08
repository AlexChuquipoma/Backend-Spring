package com.portfolio.backend.profiles.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO para actualizar el perfil de un programador
 * 
 * Incluye validaciones Jakarta Validation para garantizar
 * que los datos recibidos cumplan con los requisitos de negocio.
 * 
 * Todos los campos son opcionales (para permitir actualizaciones parciales),
 * pero si se envían, deben cumplir con las validaciones.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {

    /**
     * Título del trabajo (ej: "Full Stack Developer")
     * Validaciones:
     * - Longitud: 3-150 caracteres
     */
    @Size(min = 3, max = 150, message = "El título debe tener entre 3 y 150 caracteres")
    private String jobTitle;

    /**
     * Biografía del programador
     * Validaciones:
     * - Longitud máxima: 500 caracteres
     */
    @Size(max = 500, message = "La biografía no puede superar 500 caracteres")
    private String bio;

    /**
     * URL de la imagen de perfil
     * Validaciones:
     * - Debe ser una URL válida
     */
    @Pattern(regexp = "^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})([/\\w .-]*)*/?$", message = "La URL de la imagen no es válida")
    private String imageUrl;

    /**
     * Lista de habilidades técnicas
     * Validaciones:
     * - Máximo 50 habilidades
     * - Cada habilidad no puede estar vacía
     */
    @Size(max = 50, message = "Máximo 50 habilidades permitidas")
    private List<@NotBlank(message = "Las habilidades no pueden estar vacías") String> skills;

    /**
     * URL del perfil de GitHub
     * Validaciones:
     * - Debe ser una URL de GitHub válida
     */
    @Pattern(regexp = "^https://github\\.com/[a-zA-Z0-9_-]+/?$", message = "La URL de GitHub debe tener el formato: https://github.com/usuario")
    private String githubUrl;

    /**
     * URL del perfil de LinkedIn
     * Validaciones:
     * - Debe ser una URL de LinkedIn válida
     */
    @Pattern(regexp = "^https://(www\\.)?linkedin\\.com/in/[a-zA-Z0-9_-]+/?$", message = "La URL de LinkedIn debe tener el formato: https://linkedin.com/in/usuario")
    private String linkedinUrl;

    /**
     * URL del perfil de Instagram
     * Validaciones:
     * - Debe ser una URL de Instagram válida
     */
    @Pattern(regexp = "^https://(www\\.)?instagram\\.com/[a-zA-Z0-9_.]+/?$", message = "La URL de Instagram debe tener el formato: https://instagram.com/usuario")
    private String instagramUrl;

    /**
     * Número de WhatsApp (formato internacional)
     * Validaciones:
     * - Debe ser un número de teléfono válido
     */
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "El número de WhatsApp debe ser válido (formato internacional: +593987654321)")
    private String whatsappUrl;

    /**
     * Años de experiencia como programador
     * Validaciones:
     * - Mínimo: 0 años
     * - Máximo: 50 años
     */
    @Min(value = 0, message = "Los años de experiencia no pueden ser negativos")
    @Max(value = 50, message = "Los años de experiencia no pueden superar 50")
    private Integer yearsExperience;
}
