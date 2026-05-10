package com.maksim.problemService.dto.contest;

import com.maksim.problemService.dto.problem.ProblemSignatureResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.Instant;
import java.util.List;


@Schema(description = "Contest data")
@Data
public class ContestResponseDto {
    @Schema(description = "Contest ID")
    private Integer id;
    @Schema(description = "Contest title")
    private String title;
    @Schema(description = "ID of the contest author")
    private Integer authorId;
    @Schema(description = "Contest start time")
    private Instant startTime;
    @Schema(description = "Contest end time")
    private Instant endTime;
    @Schema(description = "Problems included in the contest")
    private List<ProblemSignatureResponseDto> problems;
}
