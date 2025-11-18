package az.devlab.paysnapservice.serviceimpl;

import az.devlab.paysnapservice.dto.UserResponseDto;
import az.devlab.paysnapservice.exception.NotFoundException;
import az.devlab.paysnapservice.mapper.UserMapper;
import az.devlab.paysnapservice.model.User;
import az.devlab.paysnapservice.repository.UserRepository;
import az.devlab.paysnapservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto getCurrentUser() {
        User user = getCurrentUserEntity();
        return userMapper.toDto(user);
    }

    @Override
    public UserResponseDto getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        return userMapper.toDto(user);
    }

    private User getCurrentUserEntity() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new NotFoundException("Current user not found");
        }

        return userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new NotFoundException("User not found with email: " + auth.getName()));
    }
}
