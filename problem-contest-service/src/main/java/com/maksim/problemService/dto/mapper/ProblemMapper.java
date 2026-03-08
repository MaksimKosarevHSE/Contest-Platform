package com.maksim.problemService.dto.mapper;

import com.maksim.problemService.dto.problem.ProblemSignature;
import com.maksim.problemService.entity.Problem;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProblemMapper {

    ProblemSignature toProblemSignature(Problem p);
}
