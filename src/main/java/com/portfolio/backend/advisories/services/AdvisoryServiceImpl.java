package com.portfolio.backend.advisories.services;

import com.portfolio.backend.advisories.dto.AdvisoryDTO;
import com.portfolio.backend.advisories.entity.Advisory;
import com.portfolio.backend.advisories.entity.enums.AdvisoryStatus;
import com.portfolio.backend.advisories.repository.AdvisoryRepository;
import com.portfolio.backend.schedules.entity.Schedule;
import com.portfolio.backend.schedules.repository.ScheduleRepository;
import com.portfolio.backend.users.entity.User;
import com.portfolio.backend.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.portfolio.backend.emails.service.EmailService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.portfolio.backend.profiles.repository.ProgrammerProfileRepository;

@Service
@RequiredArgsConstructor
public class AdvisoryServiceImpl implements AdvisoryService {

    private final AdvisoryRepository advisoryRepository;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    private final ProgrammerProfileRepository programmerProfileRepository;
    private final EmailService emailService;

    @Override
    public AdvisoryDTO createAdvisory(AdvisoryDTO dto) {
        // Programmer is a User in the Advisory entity
        User programmer = userRepository.findById(dto.getProgrammerId())
                .orElseThrow(() -> new RuntimeException("Programmer (User) not found"));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Advisory advisory = new Advisory();
        advisory.setProgrammer(programmer);
        advisory.setUser(user);
        advisory.setStatus(AdvisoryStatus.PENDING);
        advisory.setMessage(dto.getMessage());
        advisory.setDate(dto.getDate());
        advisory.setTime(dto.getTime());
        advisory.setModality(dto.getModality());

        // Update schedule status to BOOKED
        if (dto.getScheduleId() != null) {
            Schedule schedule = scheduleRepository.findById(dto.getScheduleId())
                    .orElseThrow(() -> new RuntimeException("Schedule not found"));

            if ("BOOKED".equals(schedule.getStatus())) {
                throw new RuntimeException("Schedule is already booked!");
            }

            schedule.setStatus("BOOKED");
            scheduleRepository.save(schedule);
            advisory.setSchedule(schedule); // Link Schedule to Advisory
        } else {
            throw new RuntimeException("Schedule ID must be provided for advisory creation.");
        }

        Advisory savedAdvisory = advisoryRepository.save(advisory);

        // Notify Programmer
        try {
            System.out.println("📩 AdvisoryService: Preparing to notify Programmer...");
            // Programmer (User) has the email directly
            String programmerEmail = programmer.getEmail();
            System.out.println("   Recipient Programmer Email: " + programmerEmail);

            if (programmerEmail == null || programmerEmail.isEmpty() || programmerEmail.contains("example.com")) {
                System.err.println("❌ ERROR: Programmer email is invalid or dummy! Skipping email.");
            } else {
                String subject = "Nueva Solicitud de Asesoría - CiberPortfolio";
                String text = String.format(
                        "Hola %s,\n\nTienes una nueva solicitud de asesoría de %s.\n\nFecha: %s\nHora: %s\nMensaje: %s\n\nIngresa a tu dashboard para responder.",
                        programmer.getName(), user.getName(), dto.getDate(), dto.getTime(), dto.getMessage());

                emailService.sendSimpleMessage(programmerEmail, subject, text);
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to send email to programmer: " + e.getMessage());
            e.printStackTrace();
        }

        return mapToDTO(savedAdvisory);
    }

    @Override
    public List<AdvisoryDTO> getAdvisoriesByProgrammer(Long programmerId) {
        // programmerId refers to the User ID of the programmer
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
    public List<AdvisoryDTO> getAllAdvisories() {
        return advisoryRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AdvisoryDTO updateAdvisoryStatus(Long id, String status, String responseMessage) {
        Advisory advisory = advisoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Advisory not found"));

        AdvisoryStatus newStatus = AdvisoryStatus.valueOf(status.toUpperCase());
        advisory.setStatus(newStatus);
        advisory.setResponseMessage(responseMessage);

        // If Rejected, free the schedule
        if (AdvisoryStatus.REJECTED.equals(newStatus)) {
            if (advisory.getSchedule() != null) {
                Schedule schedule = advisory.getSchedule();
                schedule.setStatus("AVAILABLE");
                scheduleRepository.save(schedule);
                System.out.println("Advisory rejected. Schedule " + schedule.getId() + " freed.");
            }
        }

        Advisory savedAdvisory = advisoryRepository.save(advisory);

        // Notify User
        try {
            System.out.println("📩 AdvisoryService: Preparing to notify User...");
            String userEmail = advisory.getUser().getEmail();
            System.out.println("   Recipient User Email: " + userEmail);

            if (userEmail == null || userEmail.isEmpty() || userEmail.contains("example.com")) {
                System.err.println("❌ ERROR: User email is invalid or dummy! Skipping email.");
            } else {
                // advisory.getProgrammer() returns User, so .getName() is valid
                String uSubject = "Actualización de tu Asesoría - CiberPortfolio";
                String uText = String.format("Hola %s,\n\nTu asesoría con %s ha sido %s.\n\nRespuesta: %s",
                        advisory.getUser().getName(), advisory.getProgrammer().getName(), status,
                        responseMessage != null ? responseMessage : "Sin notas adicionales.");

                emailService.sendSimpleMessage(userEmail, uSubject, uText);
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to send email to user: " + e.getMessage());
            e.printStackTrace();
        }

        return mapToDTO(savedAdvisory);
    }

    @Override
    public Map<String, Long> getProgrammerStats(Long programmerId) {
        // Verify programmer profile exists to ensure they are actually a programmer
        programmerProfileRepository.findByUserId(programmerId)
                .orElseThrow(() -> new RuntimeException("Programmer profile not found"));

        List<Advisory> advisories = advisoryRepository.findByProgrammerId(programmerId);

        return calculateStats(advisories);
    }

    @Override
    public Map<String, Long> getUserStats(Long userId) {
        // Verify user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Advisory> advisories = advisoryRepository.findByUserId(userId);

        return calculateStats(advisories);
    }

    private Map<String, Long> calculateStats(List<Advisory> advisories) {
        long pending = advisories.stream().filter(a -> AdvisoryStatus.PENDING.equals(a.getStatus())).count();
        long accepted = advisories.stream().filter(a -> AdvisoryStatus.ACCEPTED.equals(a.getStatus())).count();
        long rejected = advisories.stream().filter(a -> AdvisoryStatus.REJECTED.equals(a.getStatus())).count();
        long completed = advisories.stream().filter(a -> AdvisoryStatus.COMPLETED.equals(a.getStatus())).count();

        long virtual = advisories.stream().filter(a -> "VIRTUAL".equals(a.getModality())).count();
        long presencial = advisories.stream().filter(a -> "PRESENCIAL".equals(a.getModality())).count();

        Map<String, Long> stats = new HashMap<>();
        stats.put("total", (long) advisories.size());
        stats.put("pending", pending);
        stats.put("accepted", accepted);
        stats.put("rejected", rejected);
        stats.put("completed", completed);
        stats.put("virtual", virtual);
        stats.put("presencial", presencial);

        return stats;
    }

    private AdvisoryDTO mapToDTO(Advisory advisory) {
        AdvisoryDTO dto = new AdvisoryDTO();
        dto.setId(advisory.getId());
        dto.setProgrammerId(advisory.getProgrammer().getId()); // Programmer is User
        dto.setProgrammerName(advisory.getProgrammer().getName()); // Programmer is User
        dto.setUserId(advisory.getUser().getId());
        dto.setUserName(advisory.getUser().getName());
        dto.setStatus(advisory.getStatus().name());
        dto.setMessage(advisory.getMessage());
        dto.setResponseMessage(advisory.getResponseMessage());
        dto.setDate(advisory.getDate());
        dto.setTime(advisory.getTime());
        dto.setModality(advisory.getModality());
        dto.setScheduleId(advisory.getSchedule() != null ? advisory.getSchedule().getId() : null);
        return dto;
    }
}
