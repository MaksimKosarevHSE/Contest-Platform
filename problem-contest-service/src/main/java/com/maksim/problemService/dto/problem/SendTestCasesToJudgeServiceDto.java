package com.maksim.problemService.dto.problem;

import com.maksim.problemService.enums.CheckerType;
import com.maksim.problemService.enums.ProgrammingLanguage;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@AllArgsConstructor
@Data
@ToString
@NoArgsConstructor
public class SendTestCasesToJudgeServiceDto {
    private Integer problemId;

    private List<byte[]> testFilesContent;

    private List<String> testFilesNames;

    private Integer countOfTestCases;

    private CheckerType checkerType;

    private ProgrammingLanguage checkerLanguage;

    private byte[] checkerSourceCode; // optional

    public static SendTestCasesToJudgeServiceDto from(ProblemCreateDto p) {
        var target = new SendTestCasesToJudgeServiceDto();
        target.countOfTestCases = p.testCasesNum();
        target.checkerType = p.checkerType();

        if (p.checkerType() == CheckerType.CUSTOM_CHECKER) {
            try {
                target.checkerSourceCode = p.fileSourceChecker().getBytes();
            } catch (IOException e) {
                log.error("Error reading checker source code from file");
                throw new RuntimeException(e);
            }
            target.checkerLanguage = p.checkerLanguage();
        }

        target.testFilesContent = Stream.concat(p.inputTestCases().stream(), p.outputTestCases().stream())
                .map(file -> {
                    try {
                        return file.getBytes();
                    } catch (IOException e) {
                        log.error("Error reading source code from file");
                        throw new RuntimeException(e);
                    }
                }).toList();

        target.testFilesNames = Stream.concat(p.inputTestCases().stream(), p.outputTestCases().stream())
                .map(MultipartFile::getOriginalFilename).toList();

        return target;
    }
}
