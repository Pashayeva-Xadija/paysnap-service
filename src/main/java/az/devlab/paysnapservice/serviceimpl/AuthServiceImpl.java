package az.devlab.paysnapservice.serviceimpl;

import az.devlab.paysnapservice.dto.AuthResponse;
import az.devlab.paysnapservice.dto.LoginRequest;
import az.devlab.paysnapservice.dto.LogoutRequest;
import az.devlab.paysnapservice.dto.RegisterRequest;
import az.devlab.paysnapservice.dto.UserResponseDto;
import az.devlab.paysnapservice.enums.RoleName;
import az.devlab.paysnapservice.exception.AlreadyExistsException;
import az.devlab.paysnapservice.exception.BadRequestException;
import az.devlab.paysnapservice.model.Role;
import az.devlab.paysnapservice.model.User;
import az.devlab.paysnapservice.repository.RoleRepository;
import az.devlab.paysnapservice.repository.UserRepository;
import az.devlab.paysnapservice.security.JwtTokenProvider;
import az.devlab.paysnapservice.service.AuthService;
import az.devlab.paysnapservice.service.TokenBlacklistService;
import az.devlab.paysnapservice.util.TokenUtils;
import az.devlab.paysnapservice.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final TokenBlacklistService tokenBlacklistService;
    private final UserMapper userMapper;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("User already exists with email: " + request.getEmail());
        }

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new BadRequestException("Default USER role not found"));

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .roles(Set.of(userRole))
                .createdAt(LocalDateTime.now())
                .build();

        user = userRepository.save(user);

        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        UserResponseDto userDto = userMapper.toDto(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .user(userDto)
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        var authToken = new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        );
        authenticationManager.authenticate(authToken);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("User not found"));

        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);
        UserResponseDto userDto = userMapper.toDto(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .user(userDto)
                .build();
    }

    @Override
    public void logout(LogoutRequest request) {
        if (request.getAccessToken() == null || request.getAccessToken().isBlank()) {
            throw new BadRequestException("Access token is required for logout");
        }

        Instant expiresAt = jwtTokenProvider.getExpirationInstant(request.getAccessToken());
        tokenBlacklistService.blacklistToken(request.getAccessToken(), expiresAt);
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new BadRequestException("Invalid refresh token");
        }

        String email = jwtTokenProvider.getUsernameFromToken(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found"));

        String newAccessToken = jwtTokenProvider.generateAccessToken(user);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(user);
        UserResponseDto userDto = userMapper.toDto(user);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .user(userDto)
                .build();
    }
}
