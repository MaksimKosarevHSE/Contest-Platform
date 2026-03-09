package com.maksim.problemService.dto.standings;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserProgressResponseDto {
    private int userId;
    int place;
    List<TaskProgressResponseDto> taskProgress;
    private int score;
}
