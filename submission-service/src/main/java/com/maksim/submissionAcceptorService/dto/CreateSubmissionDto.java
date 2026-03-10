package com.maksim.submissionAcceptorService.dto;

import com.maksim.submissionAcceptorService.enums.ProgrammingLanguage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateSubmissionDto {
    @Schema(description = "Source code (text)")
    private String sourceCode;

    @Schema(description = "Source code (file)")
    private MultipartFile sourceFile;

    @NotNull
    @Schema(description = "Programming language")
    private ProgrammingLanguage language;
}
