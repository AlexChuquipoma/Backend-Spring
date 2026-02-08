package com.portfolio.backend.advisories.repository;

import com.portfolio.backend.advisories.entity.Advisory;
import com.portfolio.backend.advisories.entity.enums.AdvisoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AdvisoryRepository extends JpaRepository<Advisory, Long> {

    List<Advisory> findByUserId(Long userId);

    List<Advisory> findByProgrammerId(Long programmerId);

    List<Advisory> findByStatus(AdvisoryStatus status);

    List<Advisory> findByProgrammerIdAndStatus(Long programmerId, AdvisoryStatus status);

    List<Advisory> findByDateBetween(LocalDate startDate, LocalDate endDate);

    List<Advisory> findByProgrammerIdAndDateBetween(Long programmerId, LocalDate startDate, LocalDate endDate);
}
