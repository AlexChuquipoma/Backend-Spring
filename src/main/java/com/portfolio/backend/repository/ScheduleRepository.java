package com.portfolio.backend.repository;

import com.portfolio.backend.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByProgrammerId(Long programmerId);

    List<Schedule> findByProgrammerIdAndDayOfWeek(Long programmerId, DayOfWeek dayOfWeek);
}
