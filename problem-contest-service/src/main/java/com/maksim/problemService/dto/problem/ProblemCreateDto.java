package com.maksim.problemService.dto.problem;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.maksim.problemService.enums.CheckerType;
import com.maksim.problemService.enums.ProgrammingLanguage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProblemCreateDto {
    @Schema(description = "Title of problem")
    @NotBlank(message = "Title of problem is required")
    @Size(max = 50, message = "Title length must be from 1 to 50 symbol")
    private String title;

    @Schema(description = "Can this problem be displayed in public problem set?")
    @NotNull
    private Boolean isPublic;

    @Schema(description = "Problem statement")
    @NotBlank(message = "Statement of problem is required")
    @Size(max = 5000, message = "Statement length must be from 1 to 5000 symbols")
    private String statement;

    @Schema(description = "Input description")
    @Size(max = 5000, message = "Input length must be from 0 to 5000 symbols")
    private String input;

    @Schema(description = "Output description")
    @Size(max = 5000, message = "Output length must be from 0 to 5000 symbols")
    private String output;

    @Schema(description = "Notes to problem")
    @Size(max = 5000, message = "Notes length must be from 0 to 5000 symbols")
    private String notes;

    @Schema(description = "Count of samples")
    @Min(value = 0, message = "Samples count must be not negative")
    @Max(value = 15, message = "Samples count must be less than 15")
    private Integer samplesCount;

    // валидация в ProblemValidator
    @Schema(description = "i-th array's element representations i-th sample input")
    private List<String> sampleInput;

    @Schema(description = "i-th array's element representations i-th sample output")
    private List<String> sampleOutput;

    @Schema(description = "Estimated by you complexity of this problem")
    @Min(value = 1, message = "Complexity must be greater than 0 and less than 11")
    @Max(value = 10, message = "Complexity must be greater than 0 and less than 11")
    private Integer complexity;

    @Schema(description = "Compile time limit for solutions")
    @Positive
    @Max(value = 10)
    private Double compileTimeLimit;

    @Schema(description = "Time limit for solutions")
    @Positive
    @Max(value = 10)
    private Double timeLimit;

    @Schema(description = "Memory limit for solutions")
    @Positive
    @Max(value = 2048)
    private Double memoryLimit;

    @Schema(description = "Type of checker program")
    @NotNull
    private CheckerType checkerType;

    @Schema(description = "Language of custom checker (source code language)")
    private ProgrammingLanguage checkerLanguage; // если указан custom checker

    // валидация в ProblemValidator
    @Schema(description = "Custom checker's source code")
//    @JsonIgnore
    private MultipartFile fileSourceChecker; // если указан custom checker

    @Schema(description = "Number of tests")
    @Positive
    private Integer testCasesNum;

    // валидация в ProblemValidator
    @Schema(description = "Input for test in files. This files must be numbered from 1.in to N.in, where N is number of test cases")
//    @JsonIgnore
    private List<MultipartFile> inputTestCases; // must be numbered from 1 to n

    @Schema(description = "Output for test in files. This files must be numbered from 1.out to N.out, where N is number of test cases")
//    @JsonIgnore
    private List<MultipartFile> outputTestCases; // must be numbered from 1 to n
}



