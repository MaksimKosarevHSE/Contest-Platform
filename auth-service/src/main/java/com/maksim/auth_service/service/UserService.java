package com.maksim.auth_service.service;

import com.maksim.auth_service.dto.LoginRequestDto;
import com.maksim.auth_service.dto.RegisterRequestDto;
import com.maksim.auth_service.dto.TokenResponseDto;
import com.maksim.auth_service.entity.User;
import com.maksim.auth_service.exception.AuthException;
import com.maksim.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    private final JwtService jwtService;

    public TokenResponseDto login(LoginRequestDto request) {
        String email = request.email();
        String password = request.password();

        var dbUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException("User with email " + email + " is not found"));
        if (!encoder.matches(password, dbUser.getPasswordHash())) throw new AuthException("Incorrect password");
        return new TokenResponseDto(jwtService.generateToken(dbUser.getHandle(), dbUser.getId()));
    }

    public TokenResponseDto register(RegisterRequestDto registerRequest) {
        String email = registerRequest.email();
        String handle = registerRequest.handle();

        if (userRepository.existsByEmail(email))
            throw new AuthException("User with email " + email + " already exists");

        if (userRepository.existsByHandle(handle))
            throw new AuthException("User with handle " + handle + " already exists");

        User user = new User();
        user.setEmail(email);
        user.setHandle(handle);
        user.setPasswordHash(encoder.encode(registerRequest.password()));
        userRepository.save(user);
        return new TokenResponseDto(jwtService.generateToken(user.getHandle(), user.getId()));
    }
}
