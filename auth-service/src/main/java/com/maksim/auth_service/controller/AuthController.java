package com.maksim.auth_service.controller;


import com.maksim.auth_service.dto.LoginRequestDto;
import com.maksim.auth_service.dto.RegisterRequestDto;
import com.maksim.auth_service.dto.TokenResponseDto;
import com.maksim.auth_service.dto.ValidateRequestDto;
import com.maksim.auth_service.dto.ValidateResponseDto;
import com.maksim.auth_service.service.UserService;
import com.maksim.auth_service.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Сервис реализован максимально компактно и просто на одном jwt

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<TokenResponseDto> register(@RequestBody RegisterRequestDto request) {
        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @PostMapping("/validate")
    public ResponseEntity<ValidateResponseDto> validate(@RequestBody ValidateRequestDto validateRequest) {
        return ResponseEntity.ok(jwtService.validate(validateRequest.token()));
    }
}
