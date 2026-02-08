package com.portfolio.backend.advisories.services;

import com.portfolio.backend.advisories.dto.AdvisoryDTO;
import com.portfolio.backend.advisories.entity.Advisory;
import com.portfolio.backend.advisories.entity.enums.AdvisoryStatus;
import com.portfolio.backend.advisories.repository.AdvisoryRepository;
import com.portfolio.backend.users.entity.User;
import com.portfolio.backend.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdvisoryServiceImpl implements AdvisoryService {

    private final AdvisoryRepository advisoryRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public AdvisoryDTO createAdvisory(AdvisoryDTO dto) {
        User programmer = userRepository.findById(dto.getProgrammerId())
                .orElseThrow(() -> new RuntimeException("Programmer not found"));
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Advisory advisory = Advisory.builder()
                .programmer(programmer)
                .user(user)
                .status(AdvisoryStatus.PENDING)
                .message(dto.getMessage())
                .date(dto.getDate())
                .time(dto.getTime())
                .modality(dto.getModality())
                .build();

        return mapToDTO(advisoryRepository.save(advisory));
    }

    @Override
    public List<AdvisoryDTO> getAdvisoriesByProgrammer(Long programmerId) {
        return advisoryRepository.findByProgrammerId(programmerId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AdvisoryDTO> getAdvisoriesByUser(Long userId) {
        return advisoryRepository.findByUserId(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AdvisoryDTO updateAdvisoryStatus(Long id, String status) {
        Advisory advisory = advisoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Advisory not found"));
        advisory.setStatus(AdvisoryStatus.valueOf(status));
        return mapToDTO(advisoryRepository.save(advisory));
    }

    @Override
    public Map<String, Long> getProgrammerStats(Long programmerId) {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", (long) advisoryRepository.findByProgrammerId(programmerId).size());
        stats.put("pending", advisoryRepository.countByProgrammerIdAndStatus(programmerId, AdvisoryStatus.PENDING));
        stats.put("accepted", advisoryRepository.countByProgrammerIdAndStatus(programmerId, AdvisoryStatus.ACCEPTED));
        stats.put("rejected", advisoryRepository.countByProgrammerIdAndStatus(programmerId, AdvisoryStatus.REJECTED));
        stats.put("completed", advisoryRepository.countByProgrammerIdAndStatus(programmerId, AdvisoryStatus.COMPLETED));

        // Count modalities
        List<Advisory> advisories = advisoryRepository.findByProgrammerId(programmerId);
        stats.put("virtual", advisories.stream().filter(a -> "VIRTUAL".equals(a.getModality())).count());
        stats.put("presencial", advisories.stream().filter(a -> "PRESENCIAL".equals(a.getModality())).count());

        return stats;
    }

    private AdvisoryDTO mapToDTO(Advisory advisory) {
        return AdvisoryDTO.builder()
                .id(advisory.getId())
                .programmerId(advisory.getProgrammer().getId())
                .programmerName(advisory.getProgrammer().getName())
                .userId(advisory.getUser().getId())
                .userName(advisory.getUser().getName())
                .status(advisory.getStatus().name())
                .message(advisory.getMessage())
                .date(advisory.getDate())
                .time(advisory.getTime())
                .modality(advisory.getModality())
                .build();
    }
}
