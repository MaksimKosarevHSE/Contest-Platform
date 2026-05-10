package com.maksim.problemService.dto.standings;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Progress of a user on a single contest task")
public record TaskProgressResponseDto(
        @Schema(description = "Task identifier")
        Integer taskId,
        @Schema(description = "Whether the task is solved")
        Boolean solved,
        @Schema(description = "Number of submission attempts for the task")
        Integer attempts,
        @Schema(description = "Seconds elapsed from contest start until solving")
        Integer secondsAfterSolving,
        @Schema(description = "Score earned for the task")
        Integer score
) {
}
