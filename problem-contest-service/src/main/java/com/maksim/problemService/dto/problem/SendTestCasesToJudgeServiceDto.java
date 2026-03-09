package com.maksim.problemService.dto.problem;

import com.maksim.problemService.enums.CheckerType;
import com.maksim.problemService.enums.ProgrammingLanguage;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class SendTestCasesToJudgeServiceDto {
    private int problemId;
    private List<byte[]> testFilesContent;
    private List<String> testFilesNames;
    private int countOfTestCases;
    private CheckerType checkerType;
    private ProgrammingLanguage checkerLanguage;
    private byte[] checkerSourceCode; // optional

    public static SendTestCasesToJudgeServiceDto from(ProblemCreateDto p) {
        var target = new SendTestCasesToJudgeServiceDto();
        target.countOfTestCases = p.getTestCasesNum();
        target.checkerType = p.getCheckerType();

        if (p.getCheckerType() == CheckerType.CUSTOM_CHECKER) {
            try {
                target.checkerSourceCode = p.getFileSourceChecker().getBytes();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            target.checkerLanguage = p.getCheckerLanguage();
        }

        target.testFilesContent = Stream.concat(p.getInputTestCases().stream(), p.getOutputTestCases().stream())
                .map(file -> {
                    try {
                        return file.getBytes();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).toList();

        target.testFilesNames = Stream.concat(p.getInputTestCases().stream(), p.getOutputTestCases().stream())
                .map(MultipartFile::getOriginalFilename).toList();

        return target;
    }
}
