package com.portfolio.backend.repository;

import com.portfolio.backend.entity.ProgrammerProfile;
import com.portfolio.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProgrammerProfileRepository extends JpaRepository<ProgrammerProfile, Long> {

    Optional<ProgrammerProfile> findByUser(User user);

    Optional<ProgrammerProfile> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}
