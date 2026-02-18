package com.maksim.standings_service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskProgressDto {
    private int taskId;
    private boolean isSolved;
    private int attempts;
    private int hoursAfterStart;
    private int minutesAfterStart;
}
