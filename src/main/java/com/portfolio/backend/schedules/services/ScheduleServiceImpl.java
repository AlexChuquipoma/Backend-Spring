package com.portfolio.backend.schedules.services;

import com.portfolio.backend.schedules.dto.ScheduleDTO;
import com.portfolio.backend.schedules.entity.Schedule;
import com.portfolio.backend.schedules.entity.enums.Modality;
import com.portfolio.backend.schedules.repository.ScheduleRepository;
import com.portfolio.backend.users.entity.User;
import com.portfolio.backend.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ScheduleDTO createSchedule(ScheduleDTO dto) {
        User programmer = userRepository.findById(dto.getProgrammerId())
                .orElseThrow(() -> new RuntimeException("Programmer not found with ID: " + dto.getProgrammerId()));

        // TODO: Validate role is PROGRAMMER?
        // if (programmer.getRole() != Role.PROGRAMMER) ...

        LocalTime endTime = dto.getEndTime() != null ? dto.getEndTime() : dto.getTime().plusHours(1);

        Schedule schedule = Schedule.builder()
                .programmer(programmer)
                .date(dto.getDate())
                .time(dto.getTime())
                .startTime(dto.getTime())
                .endTime(endTime)
                .status("AVAILABLE")
                .modality(dto.getModality() != null ? Modality.valueOf(dto.getModality()) : Modality.VIRTUAL)
                .build();

        Schedule saved = scheduleRepository.save(schedule);
        return mapToDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScheduleDTO> getAllSchedules() {
        return scheduleRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScheduleDTO> getSchedulesByProgrammer(Long programmerId) {
        return scheduleRepository.findByProgrammerId(programmerId).stream()
                .filter(s -> "AVAILABLE".equals(s.getStatus()))
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteSchedule(Long id) {
        if (!scheduleRepository.existsById(id)) {
            throw new RuntimeException("Schedule not found with ID: " + id);
        }
        scheduleRepository.deleteById(id);
    }

    private ScheduleDTO mapToDTO(Schedule schedule) {
        return ScheduleDTO.builder()
                .id(schedule.getId())
                .programmerId(schedule.getProgrammer().getId())
                .programmerName(schedule.getProgrammer().getName())
                .date(schedule.getDate())
                .time(schedule.getTime())
                .endTime(schedule.getEndTime())
                .status(schedule.getStatus())
                .modality(schedule.getModality().name())
                .build();
    }
}
