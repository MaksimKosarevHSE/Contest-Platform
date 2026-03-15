package com.maksim.problemService.dto.standings;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskProgressResponseDto {
    private int taskId;
    private boolean solved;
    private int attempts;
    private int secondsAfterSolving;
    private int score;
}
