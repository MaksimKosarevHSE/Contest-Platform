package com.maksim.standings_service.event;

import com.maksim.standings_service.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class StandingsUpdateEvent {
    private int userId;
    private int contestId;
    private int problemId;
    private LocalDateTime submissionTime;
    private Status status;
    private int score;
    private int fine;

}
