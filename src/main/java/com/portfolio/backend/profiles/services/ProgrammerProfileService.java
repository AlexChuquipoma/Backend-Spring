package com.portfolio.backend.profiles.services;

import com.portfolio.backend.profiles.dtos.ProgrammerProfileDTO;
import com.portfolio.backend.profiles.dtos.UpdateProfileRequest;

public interface ProgrammerProfileService {
    ProgrammerProfileDTO getProfileByUserId(Long userId);

    ProgrammerProfileDTO getMyProfile(String userEmail);

    ProgrammerProfileDTO createOrUpdateProfile(String userEmail, UpdateProfileRequest request);

    void deleteProfile(String userEmail);
}
