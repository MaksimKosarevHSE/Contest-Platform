package com.maksim.problemService.dto.contest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Short information about contest")
public class ContestSignatureDto {
    private Integer id;
    private Integer authorId;
    private String authorHandle;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public ContestSignatureDto(Integer id, Integer authorId, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.authorId = authorId;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
