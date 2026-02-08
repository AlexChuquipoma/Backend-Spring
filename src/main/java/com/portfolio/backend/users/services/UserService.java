package com.portfolio.backend.users.services;

import com.portfolio.backend.users.dtos.UpdateUserRequest;
import com.portfolio.backend.users.dtos.UserDTO;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    UserDTO getMyUser(String email);

    UserDTO updateUser(String email, UpdateUserRequest request);

    UserDTO updateProfileImage(String email, MultipartFile file);

    java.util.List<UserDTO> getAllUsers();

    UserDTO updateUserRole(Long userId, String roleName);

    void deleteUser(Long userId);
}
