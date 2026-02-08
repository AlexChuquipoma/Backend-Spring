package com.portfolio.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private String jobTitle;

    @Column(columnDefinition = "TEXT")
    private String bio;

    private String imageUrl;

    @ElementCollection
    @CollectionTable(name = "programmer_skills", joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "skill")
    private List<String> skills;

    private String githubUrl;
    private String linkedinUrl;
    private String instagramUrl;
    private String whatsappUrl;

    private Integer yearsExperience;
    private Double rating;

    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;

    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = java.time.LocalDateTime.now();
        updatedAt = java.time.LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = java.time.LocalDateTime.now();
    }
}
