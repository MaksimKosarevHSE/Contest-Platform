package com.maksim.problemService.dto.standings;


import java.util.Comparator;
import java.util.List;

public record UserProgressResponseDto(
        Integer userId,
        Integer place,
        List<TaskProgressResponseDto> taskProgress,
        Integer score
) {
    public static UserProgressResponseDto of(int userId, int rank, List<TaskProgressResponseDto> tasks, int totalScore) {
        tasks.sort(Comparator.comparingInt(TaskProgressResponseDto::taskId));
        return new UserProgressResponseDto(userId, rank, tasks, totalScore);
    }
}
