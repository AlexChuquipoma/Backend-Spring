package com.portfolio.backend.schedules.services;

import com.portfolio.backend.schedules.dto.ScheduleDTO;
import java.util.List;

public interface ScheduleService {
    ScheduleDTO createSchedule(ScheduleDTO dto);

    List<ScheduleDTO> getAllSchedules();

    List<ScheduleDTO> getSchedulesByProgrammer(Long programmerId);

    void deleteSchedule(Long id);
}
