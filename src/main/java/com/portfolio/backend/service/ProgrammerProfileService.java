package com.portfolio.backend.service;

import com.portfolio.backend.dto.ProgrammerProfileDTO;
import com.portfolio.backend.dto.UpdateProfileRequest;

public interface ProgrammerProfileService {
    ProgrammerProfileDTO getProfileByUserId(Long userId);

    ProgrammerProfileDTO getMyProfile(String userEmail);

    ProgrammerProfileDTO createOrUpdateProfile(String userEmail, UpdateProfileRequest request);

    void deleteProfile(String userEmail);
}
