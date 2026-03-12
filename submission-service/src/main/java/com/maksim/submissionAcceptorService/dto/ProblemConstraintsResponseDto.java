package com.maksim.submissionAcceptorService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.*;

import java.time.LocalDateTime;

@Data
public class ProblemConstraintsResponseDto {
    private Integer id;
    private Integer compileTimeLimit;
    private Integer timeLimit;
    private Integer memoryLimit;
    // if problem is included in contest
    private Integer contestId;
    private LocalDateTime contestStartTime;
    private LocalDateTime contestEndTime;
}
