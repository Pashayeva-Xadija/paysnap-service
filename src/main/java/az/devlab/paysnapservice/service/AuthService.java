package az.devlab.paysnapservice.service;

import az.devlab.paysnapservice.dto.AuthResponse;
import az.devlab.paysnapservice.dto.LoginRequest;
import az.devlab.paysnapservice.dto.LogoutRequest;
import az.devlab.paysnapservice.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    void logout(LogoutRequest request);

    AuthResponse refreshToken(String refreshToken);
}
