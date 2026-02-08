package com.portfolio.backend.profiles.services;

import com.portfolio.backend.profiles.dtos.ProgrammerProfileDTO;
import com.portfolio.backend.profiles.dtos.UpdateProfileRequest;
import com.portfolio.backend.profiles.entity.ProgrammerProfile;
import com.portfolio.backend.profiles.repository.ProgrammerProfileRepository;
import com.portfolio.backend.users.entity.User;
import com.portfolio.backend.users.entity.enums.Role;
import com.portfolio.backend.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del servicio de perfiles de programadores.
 * Contiene la lógica de negocio para gestionar perfiles profesionales.
 *
 * @Service: Marca esta clase como un componente de servicio de Spring
 * @RequiredArgsConstructor: Lombok genera un constructor con los campos 'final'
 */
@Service
@RequiredArgsConstructor
public class ProgrammerProfileServiceImpl implements ProgrammerProfileService {

        // Inyección de dependencias automática por constructor (gracias a
        // @RequiredArgsConstructor)
        private final ProgrammerProfileRepository profileRepository;
        private final UserRepository userRepository;

        /**
         * Obtiene el perfil de un programador por su ID de usuario.
         * Endpoint público - No requiere autenticación.
         *
         * @param userId ID del usuario cuyo perfil se quiere obtener
         * @return DTO con la información del perfil
         * @throws RuntimeException si el usuario o perfil no existe
         */
        @Override
        public ProgrammerProfileDTO getProfileByUserId(Long userId) {
                // Buscar usuario por ID, lanzar excepción si no existe
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                // Buscar perfil asociado al usuario
                ProgrammerProfile profile = profileRepository.findByUser(user)
                                .orElseThrow(() -> new RuntimeException("Profile not found"));

                // Convertir entidad a DTO antes de retornar
                return convertToDTO(profile);
        }

        /**
         * Obtiene el perfil del usuario autenticado actual.
         * Requiere autenticación - El email viene del token JWT.
         *
         * @param userEmail Email del usuario autenticado (extraído del JWT)
         * @return DTO con la información del perfil
         * @throws RuntimeException si el usuario o perfil no existe
         */
        @Override
        public ProgrammerProfileDTO getMyProfile(String userEmail) {
                // Buscar usuario por email (viene del token JWT)
                User user = userRepository.findByEmail(userEmail)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                // Buscar perfil, mensaje específico si no existe
                ProgrammerProfile profile = profileRepository.findByUser(user)
                                .orElseThrow(() -> new RuntimeException("Profile not found. Please create one first."));

                return convertToDTO(profile);
        }

        /**
         * Crea o actualiza el perfil del usuario autenticado.
         *
         * Lógica:
         * 1. Valida que el usuario tenga rol PROGRAMMER
         * 2. Busca si ya existe un perfil
         * 3. Si existe: actualiza los campos
         * 4. Si NO existe: crea uno nuevo
         * 5. Guarda en la base de datos
         *
         * @Transactional: Garantiza que toda la operación se ejecute en una transacción
         *                 Si algo falla, se hace rollback automático
         *
         * @param userEmail Email del usuario autenticado
         * @param request   Datos del perfil a crear/actualizar
         * @return DTO con el perfil guardado
         * @throws RuntimeException si el usuario no existe o no tiene rol PROGRAMMER
         */
        @Override
        @Transactional
        public ProgrammerProfileDTO createOrUpdateProfile(String userEmail, UpdateProfileRequest request) {
                // 1. Buscar usuario por email
                User user = userRepository.findByEmail(userEmail)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                // 2. VALIDACIÓN DE SEGURIDAD: Solo usuarios con rol PROGRAMMER pueden tener
                // perfiles
                if (user.getRole() != Role.PROGRAMMER) {
                        throw new RuntimeException("Only users with PROGRAMMER role can create profiles");
                }

                // 3. Buscar perfil existente o crear uno nuevo
                // .orElse(): Si no existe, crea un nuevo perfil con valores por defecto
                ProgrammerProfile profile = profileRepository.findByUser(user)
                                .orElse(ProgrammerProfile.builder()
                                                .user(user)
                                                .rating(0.0) // Rating inicial en 0
                                                .build());

                // 4. Actualizar todos los campos del perfil con los datos del request
                profile.setJobTitle(request.getJobTitle());
                profile.setBio(request.getBio());
                profile.setImageUrl(request.getImageUrl());
                profile.setSkills(request.getSkills());
                profile.setGithubUrl(request.getGithubUrl());
                profile.setLinkedinUrl(request.getLinkedinUrl());
                profile.setInstagramUrl(request.getInstagramUrl());
                profile.setWhatsappUrl(request.getWhatsappUrl());
                profile.setYearsExperience(request.getYearsExperience());

                // 5. Guardar en la base de datos (INSERT si es nuevo, UPDATE si existe)
                ProgrammerProfile savedProfile = profileRepository.save(profile);

                // 6. Convertir a DTO y retornar
                return convertToDTO(savedProfile);
        }

        /**
         * Elimina el perfil del usuario autenticado.
         *
         * @Transactional: Garantiza que la eliminación sea atómica
         *
         * @param userEmail Email del usuario autenticado
         * @throws RuntimeException si el usuario o perfil no existe
         */
        @Override
        @Transactional
        public void deleteProfile(String userEmail) {
                // Buscar usuario
                User user = userRepository.findByEmail(userEmail)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                // Buscar perfil
                ProgrammerProfile profile = profileRepository.findByUser(user)
                                .orElseThrow(() -> new RuntimeException("Profile not found"));

                // Eliminar de la base de datos
                profileRepository.delete(profile);
        }

        /**
         * Método privado para convertir una entidad ProgrammerProfile a DTO.
         *
         * ¿Por qué usar DTOs?
         * - Seguridad: No exponemos la entidad completa con sus relaciones
         * - Control: Decidimos exactamente qué datos enviar al frontend
         * - Desacoplamiento: Podemos cambiar la entidad sin afectar la API
         *
         * @param profile Entidad de perfil
         * @return DTO con los datos del perfil
         */
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
