package com.portfolio.backend.profiles.entity;

import com.portfolio.backend.users.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Entidad que representa el perfil profesional de un programador.
 * Contiene información detallada sobre habilidades, experiencia y redes
 * sociales.
 *
 * Relación: Un usuario (User) puede tener UN SOLO perfil de programador
 * (OneToOne).
 */
@Entity
@Table(name = "programmer_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgrammerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Relación OneToOne con User.
     * - Un perfil pertenece a UN SOLO usuario
     * - Un usuario puede tener UN SOLO perfil
     * - unique = true: Garantiza que no haya dos perfiles para el mismo usuario
     * - nullable = false: El perfil DEBE tener un usuario asociado
     */
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // Título profesional (ej: "Full Stack Developer", "Backend Engineer")
    private String jobTitle;

    /**
     * Biografía del programador.
     * columnDefinition = "TEXT": Permite textos largos (más de 255 caracteres)
     */
    @Column(columnDefinition = "TEXT")
    private String bio;

    // URL de la imagen de perfil
    private String imageUrl;

    /**
     * Lista de habilidades del programador (ej: ["Java", "Spring Boot",
     * "PostgreSQL"])
     *
     * @ElementCollection: Indica que es una colección de elementos simples (String)
     * @CollectionTable: Crea una tabla separada "programmer_skills" para almacenar
     *                   las habilidades
     *
     *                   Estructura en BD:
     *                   - Tabla: programmer_skills
     *                   - Columnas: profile_id (FK), skill (String)
     */
    @ElementCollection
    @CollectionTable(name = "programmer_skills", joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "skill")
    private List<String> skills;

    // Enlaces a redes sociales y contacto
    private String githubUrl;
    private String linkedinUrl;
    private String instagramUrl;
    private String whatsappUrl;

    // Años de experiencia como programador
    private Integer yearsExperience;

    // Calificación promedio (0.0 - 5.0), calculada a partir de reviews
    private Double rating;

    /**
     * Timestamps automáticos para auditoría
     */
    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;

    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;

    /**
     * @PrePersist: Se ejecuta ANTES de guardar por primera vez en la BD
     *              Inicializa las fechas de creación y actualización
     */
    @PrePersist
    protected void onCreate() {
        createdAt = java.time.LocalDateTime.now();
        updatedAt = java.time.LocalDateTime.now();
    }

    /**
     * @PreUpdate: Se ejecuta ANTES de actualizar un registro existente
     *             Actualiza solo la fecha de modificación
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = java.time.LocalDateTime.now();
    }
}
