package com.maksim.submissionAcceptorService.event;

import com.maksim.submissionAcceptorService.enums.Status;
import lombok.*;

@Data
@AllArgsConstructor
public class SolutionJudgedEvent {
    private Long submissionId;
    private Status status;
    private Integer testNum;
    private Integer memory;
    private Integer executionTime;
    private String checkerMessage;
}
