package az.devlab.paysnapservice.mapper;

import az.devlab.paysnapservice.dto.AuthResponse;
import az.devlab.paysnapservice.dto.UserResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    @Mapping(target = "accessToken", ignore = true)
    @Mapping(target = "refreshToken", ignore = true)
    @Mapping(target = "tokenType", ignore = true)
    @Mapping(target = "user", expression = "java(userResponseDto)")
    AuthResponse toAuthResponse(UserResponseDto userResponseDto);
}

