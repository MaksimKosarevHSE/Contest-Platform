package com.maksim.problemService.dto.problem;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Short problem information used in lists")
public record ProblemSignatureResponseDto(
        @Schema(description = "Problem identifier")
        Integer id,

        @Schema(description = "Problem title")
        String title,

        @Schema(description = "Problem complexity")
        Integer complexity
) {
}
