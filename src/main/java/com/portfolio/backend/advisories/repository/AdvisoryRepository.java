package com.portfolio.backend.advisories.repository;

import com.portfolio.backend.advisories.entity.Advisory;
import com.portfolio.backend.advisories.entity.enums.AdvisoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvisoryRepository extends JpaRepository<Advisory, Long> {
    List<Advisory> findByProgrammerId(Long programmerId);

    List<Advisory> findByUserId(Long userId);

    long countByProgrammerIdAndStatus(Long programmerId, AdvisoryStatus status);
}
