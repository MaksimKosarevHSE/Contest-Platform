package com.maksim.testingService.event;

import com.maksim.testingService.enums.Status;
import lombok.*;

@Data
@Builder
public class TestCaseJudgedEvent {
    private Long submissionId;
    private Status status;
    private Integer testNum;
    private Integer memory;
    private Integer executionTime;
    private String checkerMessage;
}
