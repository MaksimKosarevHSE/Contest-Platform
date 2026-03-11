package com.maksim.submissionAcceptorService.dto;

import com.maksim.submissionAcceptorService.enums.ProgrammingLanguage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;


@Data
@Schema(description = "Body for new submission")
public class CreateSubmissionDto {
    @Schema(description = "Source code (text)", example = "print('Hello world!')")
    private String sourceCode;

    @Schema(description = "Source code (file)")
    private MultipartFile sourceFile;

    @NotNull
    @Schema(description = "Programming language", example = "CPP")
    private ProgrammingLanguage language;
}
