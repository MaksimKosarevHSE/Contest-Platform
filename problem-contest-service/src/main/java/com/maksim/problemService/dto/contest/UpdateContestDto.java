package com.maksim.problemService.dto.contest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.Instant;
import java.util.List;

@Schema(description = "Fields that can be updated for an existing contest")
public record UpdateContestDto(
        @Schema(description = "Contest title")
        @Size(max = 255)
        String title,

        @Schema(description = "Contest start time")
        @Future
        Instant startTime,

        @Schema(description = "Contest end time")
        @Future
        Instant endTime,

        @Schema(description = "List of problem's IDs included in the contest")
        List<@Positive Integer> problemsId
) {
}
