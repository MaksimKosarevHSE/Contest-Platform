package com.maksim.submissionAcceptorService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "Problem constraints and contest info (if present)")
public class ProblemConstraintsResponseDto {
    private int id;
    private double compileTimeLimit;
    private double timeLimit;
    private double memoryLimit;
    // if problem is included in contest
    private Integer contestId;
    private LocalDateTime contestStartTime;
    private LocalDateTime contestEndTime;
}
