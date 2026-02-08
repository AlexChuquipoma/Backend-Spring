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
}
