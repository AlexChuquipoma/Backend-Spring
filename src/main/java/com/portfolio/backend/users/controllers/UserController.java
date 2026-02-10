package com.portfolio.backend.users.controllers;

import com.portfolio.backend.users.dtos.UpdateUserRequest;
import com.portfolio.backend.users.dtos.UserDTO;
import com.portfolio.backend.users.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Usuarios", description = "Gestion de usuarios, imagen de perfil y roles")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getMyUser(Authentication authentication) {
        return ResponseEntity.ok(userService.getMyUser(authentication.getName()));
    }

    @PutMapping("/me")
    public ResponseEntity<UserDTO> updateUser(Authentication authentication,
            @Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateUser(authentication.getName(), request));
    }

    @PostMapping("/me/image")
    public ResponseEntity<UserDTO> updateProfileImage(Authentication authentication,
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(userService.updateProfileImage(authentication.getName(), file));
    }

    @GetMapping
    public ResponseEntity<java.util.List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<UserDTO> updateUserRole(@PathVariable Long id, @RequestParam String role) {
        return ResponseEntity.ok(userService.updateUserRole(id, role));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
