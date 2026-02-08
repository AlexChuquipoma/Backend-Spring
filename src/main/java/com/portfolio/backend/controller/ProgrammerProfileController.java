package com.portfolio.backend.controller;

import com.portfolio.backend.dto.ProgrammerProfileDTO;
import com.portfolio.backend.dto.UpdateProfileRequest;
import com.portfolio.backend.service.ProgrammerProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProgrammerProfileController {

    private final ProgrammerProfileService profileService;

    /**
     * Get current user's profile (requires authentication)
     */
    @GetMapping("/me")
    public ResponseEntity<ProgrammerProfileDTO> getMyProfile(Authentication authentication) {
        String userEmail = authentication.getName();
        ProgrammerProfileDTO profile = profileService.getMyProfile(userEmail);
        return ResponseEntity.ok(profile);
    }

    /**
     * Get profile by user ID (public access)
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<ProgrammerProfileDTO> getProfileByUserId(@PathVariable Long userId) {
        ProgrammerProfileDTO profile = profileService.getProfileByUserId(userId);
        return ResponseEntity.ok(profile);
    }

    /**
     * Create or update profile (requires authentication, PROGRAMMER role only)
     */
    @PostMapping
    public ResponseEntity<ProgrammerProfileDTO> createOrUpdateProfile(
            @RequestBody UpdateProfileRequest request,
            Authentication authentication) {
        String userEmail = authentication.getName();
        ProgrammerProfileDTO profile = profileService.createOrUpdateProfile(userEmail, request);
        return ResponseEntity.ok(profile);
    }

    /**
     * Update profile (alias for POST, follows PUT semantics)
     */
    @PutMapping
    public ResponseEntity<ProgrammerProfileDTO> updateProfile(
            @RequestBody UpdateProfileRequest request,
            Authentication authentication) {
        String userEmail = authentication.getName();
        ProgrammerProfileDTO profile = profileService.createOrUpdateProfile(userEmail, request);
        return ResponseEntity.ok(profile);
    }

    /**
     * Delete profile (requires authentication)
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteProfile(Authentication authentication) {
        String userEmail = authentication.getName();
        profileService.deleteProfile(userEmail);
        return ResponseEntity.noContent().build();
    }
}
