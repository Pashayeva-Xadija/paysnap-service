package az.devlab.paysnapservice.service;

import az.devlab.paysnapservice.dto.UserResponseDto;

public interface UserService {

    UserResponseDto getCurrentUser();

    UserResponseDto getById(Long id);
}
