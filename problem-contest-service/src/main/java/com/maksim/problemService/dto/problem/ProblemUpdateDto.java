package com.maksim.problemService.dto.problem;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Fields that can be updated for an existing problem")
public record ProblemUpdateDto(
        @Schema(description = "Updated problem title")
        String title,
        @Schema(description = "Updated problem statement")
        String statement,
        @Schema(description = "Updated input format description")
        String input,
        @Schema(description = "Updated output format description")
        String output,
        @Schema(description = "Updated additional notes")
        String notes,
        @Schema(description = "Updated number of sample tests")
        Integer samplesCount,
        @Schema(description = "Updated sample input values")
        List<String> sampleInput,
        @Schema(description = "Updated sample output values")
        List<String> sampleOutput,
        @Schema(description = "Updated estimated problem complexity")
        Integer complexity
) {
}
