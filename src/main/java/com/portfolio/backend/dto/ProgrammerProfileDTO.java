package com.portfolio.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgrammerProfileDTO {
    private Long id;
    private Long userId;
    private String userName;
    private String userEmail;
    private String jobTitle;
    private String bio;
    private String imageUrl;
    private List<String> skills;
    private String githubUrl;
    private String linkedinUrl;
    private String instagramUrl;
    private String whatsappUrl;
    private Integer yearsExperience;
    private Double rating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
