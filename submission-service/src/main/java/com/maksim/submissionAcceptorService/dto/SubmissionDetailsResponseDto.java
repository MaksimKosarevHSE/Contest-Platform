package com.maksim.submissionAcceptorService.dto;

import com.maksim.submissionAcceptorService.enums.ProgrammingLanguage;
import com.maksim.submissionAcceptorService.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Full submission information")
public class SubmissionDetailsResponseDto {
    private long id;
    private int userId;
    private int problemId;
    private Integer contestId;
    private boolean isUpsolving;
    private LocalDateTime time;
    private String source;
    private ProgrammingLanguage programmingLanguage;
    private Status status;
    private int executionTime;
    private int usedMemory;
    private int testNum;
//    private String input;
//    private String output; временно для простоты убрал
    private String checkerMessage;
}
