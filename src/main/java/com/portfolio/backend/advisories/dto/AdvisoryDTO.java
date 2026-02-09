package com.portfolio.backend.advisories.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdvisoryDTO {
    private Long id;
    private Long programmerId;
    private String programmerName;
    private Long userId;
    private String userName;
    private String status;
    private String message;
    private LocalDate date;
    private LocalTime time;
    private String modality;
    private String responseMessage;
    private Long scheduleId; // To link and update schedule status
}
