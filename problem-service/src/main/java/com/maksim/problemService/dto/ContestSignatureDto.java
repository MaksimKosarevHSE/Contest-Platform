package com.maksim.problemService.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ContestSignatureDto{
    private int id;
    private int authorId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isRunning;
}
