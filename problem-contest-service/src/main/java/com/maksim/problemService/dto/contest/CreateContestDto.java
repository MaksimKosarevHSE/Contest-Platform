package com.maksim.problemService.dto.contest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.Instant;
import java.util.List;


@Schema(description = "Contest data")
public record CreateContestDto(
        @Schema(description = "Title", example = "Кубок Трёх Флешек")
        @NotBlank(message = "Required")
        @Size(max = 255, message = "Title must be less or equal to 255")
        String title,

        @Schema(description = "Start time", example = "2027-04-01T10:00:00Z")
        @NotNull(message = "Required")
        @Future(message = "Must be in future")
        Instant startTime,

        @Schema(description = "End time", example = "2028-04-01T12:00:00Z")
        @NotNull(message = "Required")
        @Future(message = "Must be in future")
        Instant endTime,

        @Schema(description = "List of problem's IDs included in the contest")
        @NotEmpty(message = "Required")
        List<@Positive Integer> problemsId
) {
}
