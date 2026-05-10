package com.maksim.problemService.dto.standings;


import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Comparator;
import java.util.List;

@Schema(description = "Aggregated contest progress for a user")
public record UserProgressResponseDto(
        @Schema(description = "User identifier")
        Integer userId,
        @Schema(description = "Current place in standings")
        Integer place,
        @Schema(description = "Per-task progress details")
        List<TaskProgressResponseDto> taskProgress,
        @Schema(description = "Total score of the user")
        Integer score
) {
    public static UserProgressResponseDto of(int userId, int rank, List<TaskProgressResponseDto> tasks, int totalScore) {
        tasks.sort(Comparator.comparingInt(TaskProgressResponseDto::taskId));
        return new UserProgressResponseDto(userId, rank, tasks, totalScore);
    }
}
