package com.maksim.testingService.event;

import com.maksim.testingService.enums.Status;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
public class SolutionJudgedEvent {
    private long submissionId;
    private Status status;
    private int testNum;
    private int memory;
    private int executionTime;
    private String checkerMessage;
}
