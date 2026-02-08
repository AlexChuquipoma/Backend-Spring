package com.portfolio.backend.advisories.services;

import com.portfolio.backend.advisories.dto.AdvisoryDTO;

import java.util.List;
import java.util.Map;

public interface AdvisoryService {
    AdvisoryDTO createAdvisory(AdvisoryDTO dto);

    List<AdvisoryDTO> getAdvisoriesByProgrammer(Long programmerId);

    List<AdvisoryDTO> getAdvisoriesByUser(Long userId);

    AdvisoryDTO updateAdvisoryStatus(Long id, String status);

    Map<String, Long> getProgrammerStats(Long programmerId);
}
