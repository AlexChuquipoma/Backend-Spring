package com.portfolio.backend.schedules.controllers;

import com.portfolio.backend.schedules.dto.ScheduleDTO;
import com.portfolio.backend.schedules.services.ScheduleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Horarios", description = "Gestion de horarios de disponibilidad del programador")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<ScheduleDTO> createSchedule(@RequestBody ScheduleDTO dto) {
        // TODO: Validate admin role?
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.createSchedule(dto));
    }

    @GetMapping
    public ResponseEntity<List<ScheduleDTO>> getAllSchedules() {
        return ResponseEntity.ok(scheduleService.getAllSchedules());
    }

    @GetMapping("/programmer/{programmerId}")
    public ResponseEntity<List<ScheduleDTO>> getByProgrammer(@PathVariable Long programmerId) {
        return ResponseEntity.ok(scheduleService.getSchedulesByProgrammer(programmerId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }
}
