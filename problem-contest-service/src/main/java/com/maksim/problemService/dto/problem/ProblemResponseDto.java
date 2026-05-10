package com.maksim.problemService.dto.problem;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Full problem data returned to clients")
public record ProblemResponseDto(
        @Schema(description = "Problem identifier")
        Integer id,
        @Schema(description = "Problem title")
        String title,
        @Schema(description = "Problem statement")
        String statement,
        @Schema(description = "Input format description")
        String input,
        @Schema(description = "Output format description")
        String output,
        @Schema(description = "Additional notes for the problem")
        String notes,
        @Schema(description = "Number of sample tests")
        Integer samplesCount,
        @Schema(description = "Sample input values")
        List<String> sampleInput,
        @Schema(description = "Sample output values")
        List<String> sampleOutput,
        @Schema(description = "Estimated problem complexity")
        Integer complexity,
        @Schema(description = "Compilation time limit in milliseconds")
        Integer compileTimeLimit,
        @Schema(description = "Execution time limit in milliseconds")
        Integer timeLimit,
        @Schema(description = "Memory limit")
        Integer memoryLimit,
        @Schema(description = "Identifier of the problem creator")
        Integer creatorId,
        @Schema(description = "Whether the problem is visible in the public problem set")
        Boolean isPublic
) {
}
