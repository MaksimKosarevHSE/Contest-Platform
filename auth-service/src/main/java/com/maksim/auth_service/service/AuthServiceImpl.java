package com.maksim.auth_service.service;

import com.maksim.auth_service.dto.AuthResponse;
import com.maksim.auth_service.dto.LoginRequest;
import com.maksim.auth_service.dto.RegisterRequest;
import com.maksim.auth_service.dto.ValidateRequest;
import com.maksim.auth_service.dto.ValidateResponse;
import com.maksim.auth_service.entity.User;
import com.maksim.auth_service.exception.ConflictException;
import com.maksim.auth_service.exception.UnauthorizedException;
import com.maksim.auth_service.mapper.UserMapper;
import com.maksim.auth_service.repository.UserRepository;
import com.maksim.auth_service.security.JwtService;
import com.maksim.auth_service.security.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final RefreshTokenService refreshTokenService;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        User user = userMapper.toEntity(request);
        checkEmailNotExists(user.getEmail());
        checkHandleNotExists(user.getHandle());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);
        UUID refreshToken = refreshTokenService.create(user.getId(), user.getHandle());
        String accessToken = jwtService.generateAccessToken(user.getId(), user.getHandle());
        return AuthResponse.of(accessToken, refreshToken);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UnauthorizedException("User with email " + request.email() + " does not exists"));

        if (!passwordEncoder.matches(request.password(), user.getPassword()))
            throw new UnauthorizedException("Invalid password");

        UUID refreshToken = refreshTokenService.create(user.getId(), user.getHandle());
        String accessToken = jwtService.generateAccessToken(user.getId(), user.getHandle());
        return AuthResponse.of(accessToken, refreshToken);
    }

    @Override
    public AuthResponse refresh(UUID refresh) {
        RefreshTokenService.RefreshTokenData tokenData = refreshTokenService.getTokenData(refresh)
                .orElseThrow(() -> new UnauthorizedException("Invalid token"));

        String accessToken = jwtService.generateAccessToken(tokenData.userId(), tokenData.handle());

        return AuthResponse.of(accessToken, refresh);
    }

    @Override
    public ValidateResponse validate(ValidateRequest request) {
        JwtService.JwtTokenData data = jwtService.parseToken(request.token())
                .orElseThrow(() -> new UnauthorizedException("Invalid token"));
        return new ValidateResponse(data.userId(), data.handle());
    }


    @Override
    public void logout(UUID refresh) {
        refreshTokenService.delete(refresh);
    }

    private void checkEmailNotExists(String email) {
        if (userRepository.existsByEmail(email))
            throw new ConflictException("User with email " + email + " already exists");

    }

    private void checkHandleNotExists(String handle){
        if (userRepository.existsByHandle(handle))
            throw new ConflictException("User with handle " + handle + " already exists");
    }
}
