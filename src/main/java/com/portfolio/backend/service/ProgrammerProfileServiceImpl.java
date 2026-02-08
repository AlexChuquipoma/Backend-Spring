package com.portfolio.backend.service;

import com.portfolio.backend.dto.ProgrammerProfileDTO;
import com.portfolio.backend.dto.UpdateProfileRequest;
import com.portfolio.backend.entity.ProgrammerProfile;
import com.portfolio.backend.entity.User;
import com.portfolio.backend.entity.enums.Role;
import com.portfolio.backend.repository.ProgrammerProfileRepository;
import com.portfolio.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProgrammerProfileServiceImpl implements ProgrammerProfileService {

    private final ProgrammerProfileRepository profileRepository;
    private final UserRepository userRepository;

    @Override
    public ProgrammerProfileDTO getProfileByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ProgrammerProfile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        return convertToDTO(profile);
    }

    @Override
    public ProgrammerProfileDTO getMyProfile(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ProgrammerProfile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Profile not found. Please create one first."));

        return convertToDTO(profile);
    }

    @Override
    @Transactional
    public ProgrammerProfileDTO createOrUpdateProfile(String userEmail, UpdateProfileRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Only PROGRAMMER role can have profiles
        if (user.getRole() != Role.PROGRAMMER) {
            throw new RuntimeException("Only users with PROGRAMMER role can create profiles");
        }

        // Find existing profile or create new one
        ProgrammerProfile profile = profileRepository.findByUser(user)
                .orElse(ProgrammerProfile.builder()
                        .user(user)
                        .rating(0.0)
                        .build());

        // Update profile fields
        profile.setJobTitle(request.getJobTitle());
        profile.setBio(request.getBio());
        profile.setImageUrl(request.getImageUrl());
        profile.setSkills(request.getSkills());
        profile.setGithubUrl(request.getGithubUrl());
        profile.setLinkedinUrl(request.getLinkedinUrl());
        profile.setInstagramUrl(request.getInstagramUrl());
        profile.setWhatsappUrl(request.getWhatsappUrl());
        profile.setYearsExperience(request.getYearsExperience());

        ProgrammerProfile savedProfile = profileRepository.save(profile);
        return convertToDTO(savedProfile);
    }

    @Override
    @Transactional
    public void deleteProfile(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ProgrammerProfile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        profileRepository.delete(profile);
    }

    private ProgrammerProfileDTO convertToDTO(ProgrammerProfile profile) {
        return ProgrammerProfileDTO.builder()
                .id(profile.getId())
                .userId(profile.getUser().getId())
                .userName(profile.getUser().getName())
                .userEmail(profile.getUser().getEmail())
                .jobTitle(profile.getJobTitle())
                .bio(profile.getBio())
                .imageUrl(profile.getImageUrl())
                .skills(profile.getSkills())
                .githubUrl(profile.getGithubUrl())
                .linkedinUrl(profile.getLinkedinUrl())
                .instagramUrl(profile.getInstagramUrl())
                .whatsappUrl(profile.getWhatsappUrl())
                .yearsExperience(profile.getYearsExperience())
                .rating(profile.getRating())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }
}
