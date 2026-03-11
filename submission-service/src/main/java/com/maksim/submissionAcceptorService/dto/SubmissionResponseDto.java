package com.maksim.submissionAcceptorService.dto;

import com.maksim.submissionAcceptorService.enums.ProgrammingLanguage;
import com.maksim.submissionAcceptorService.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Schema(description = "Short submission information")
public class SubmissionResponseDto {
    @Schema(description = "Submission ID", example = "12345")
    private long id;

    @Schema(description = "Sender's ID", example = "42")
    private int userId;

    @Schema(description = "Problem ID", example = "101")
    private int problemId;

    @Schema(description = "Submission time", example = "2026-01-01T00:00:00")
    private LocalDateTime time;

    @Schema(description = "Programming language", example = "CPP")
    private ProgrammingLanguage programmingLanguage;

    @Schema(description = "Verdict", example = "OK")
    private Status status;

    @Schema(description = "Execution time (ms)", example = "150")
    private int executionTime;

    @Schema(description = "Used memory (KB)", example = "10240")
    private int usedMemory;
}
