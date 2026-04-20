package com.maksim.auth_service.mapper;

import com.maksim.auth_service.dto.RegisterRequest;
import com.maksim.auth_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    User toEntity(RegisterRequest req);
}
