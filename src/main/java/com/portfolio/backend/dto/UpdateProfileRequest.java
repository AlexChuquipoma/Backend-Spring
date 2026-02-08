package com.portfolio.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {
    private String jobTitle;
    private String bio;
    private String imageUrl;
    private List<String> skills;
    private String githubUrl;
    private String linkedinUrl;
    private String instagramUrl;
    private String whatsappUrl;
    private Integer yearsExperience;
}
