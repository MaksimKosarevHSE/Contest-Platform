package com.maksim.auth_service.service;


import com.maksim.auth_service.dto.AuthResponse;
import com.maksim.auth_service.dto.LoginRequest;
import com.maksim.auth_service.dto.RegisterRequest;
import com.maksim.auth_service.dto.ValidateRequest;
import com.maksim.auth_service.dto.ValidateResponse;

import java.util.UUID;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refresh(UUID refresh);

    ValidateResponse validate(ValidateRequest request);

    void logout(UUID refresh);
}
