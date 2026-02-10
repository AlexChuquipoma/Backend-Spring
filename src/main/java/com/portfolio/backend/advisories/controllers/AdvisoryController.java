package com.portfolio.backend.advisories.controllers;

import com.portfolio.backend.advisories.dto.AdvisoryDTO;
import com.portfolio.backend.advisories.services.AdvisoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/advisories")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Asesorias", description = "Solicitud, gestion y estadisticas de asesorias")
public class AdvisoryController {

    private final AdvisoryService advisoryService;

    @PostMapping
    public ResponseEntity<AdvisoryDTO> createAdvisory(@RequestBody AdvisoryDTO dto) {
        return ResponseEntity.ok(advisoryService.createAdvisory(dto));
    }

    @GetMapping("/programmer/{id}")
    public ResponseEntity<List<AdvisoryDTO>> getByProgrammer(@PathVariable Long id) {
        return ResponseEntity.ok(advisoryService.getAdvisoriesByProgrammer(id));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<AdvisoryDTO>> getByUser(@PathVariable Long id) {
        return ResponseEntity.ok(advisoryService.getAdvisoriesByUser(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<AdvisoryDTO>> getAll() {
        return ResponseEntity.ok(advisoryService.getAllAdvisories());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<AdvisoryDTO> updateStatus(@PathVariable Long id, @RequestParam String status,
            @RequestParam(required = false) String responseMessage) {
        return ResponseEntity.ok(advisoryService.updateAdvisoryStatus(id, status, responseMessage));
    }

    @GetMapping("/stats/programmer/{id}")
    public ResponseEntity<Map<String, Long>> getStats(@PathVariable Long id) {
        return ResponseEntity.ok(advisoryService.getProgrammerStats(id));
    }

    @GetMapping("/stats/user/{id}")
    public ResponseEntity<Map<String, Long>> getUserStats(@PathVariable Long id) {
        return ResponseEntity.ok(advisoryService.getUserStats(id));
    }
}
