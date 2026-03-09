package com.maksim.problemService.dto.problem;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Schema(description = "Полное описание задачи")
public record ProblemSignatureResponseDto(
        Integer id,
        String title,
        int complexity
) {
}
