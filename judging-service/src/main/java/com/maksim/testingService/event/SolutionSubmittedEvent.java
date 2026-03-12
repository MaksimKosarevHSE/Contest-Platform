package com.maksim.testingService.event;


import com.maksim.testingService.enums.ProgrammingLanguage;
import lombok.*;


@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@ToString
public class SolutionSubmittedEvent {
    private Integer problemId;
    private Integer contestId;
    private Integer userId;
    private Long submissionId;
    private String source;
    private ProgrammingLanguage language;
    private Integer timeLimit;
    private Integer memoryLimit;
    private Integer compilationTimeLimit;
}