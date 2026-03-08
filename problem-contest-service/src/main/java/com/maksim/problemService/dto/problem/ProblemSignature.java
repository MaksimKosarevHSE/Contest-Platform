package com.maksim.problemService.dto.problem;


import com.maksim.problemService.entity.Problem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Schema(description = "Полное описание задачи")
public record ProblemSignature(
        Integer id,
        String title,
        int complexity
) {
//    public static ProblemSignature from(Problem problem) {
//        return new ProblemSignature(problem.getId(), problem.getTitle(),problem.getComplexity());
//    }
}
