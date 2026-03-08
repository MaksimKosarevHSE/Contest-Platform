package com.maksim.problemService.dto.mapper;

import com.maksim.problemService.dto.contest.CreateContestDto;
import com.maksim.problemService.entity.Contest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ContestMapper {

    @Mapping(target="id", ignore = true)
    @Mapping(target="authorId", ignore = true)
    Contest toEntity(CreateContestDto dto);
}
