package com.maksim.testingService.service.model;


import com.maksim.testingService.dto.SaveTestCasesDto;
import com.maksim.testingService.enums.CheckerType;
import com.maksim.testingService.enums.ProgrammingLanguage;
import lombok.*;


@Data
public class TestCasesMetadata {
    private int problemId;
    private int testCount;
    private CheckerType checkerType;
    private ProgrammingLanguage checkerLanguage;
    private String checkerFileName;

    public static TestCasesMetadata from(SaveTestCasesDto dto) {
        var metadata = new TestCasesMetadata();
        metadata.setProblemId(dto.getProblemId());
        metadata.setTestCount(dto.getCountOfTestCases());
        metadata.setCheckerType(dto.getCheckerType());
        metadata.setCheckerLanguage(dto.getCheckerLanguage());
        return metadata;
    }
}
