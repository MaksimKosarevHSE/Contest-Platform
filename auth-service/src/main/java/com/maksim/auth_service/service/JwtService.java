package com.maksim.auth_service.service;

import com.maksim.auth_service.dto.ValidateResponseDto;
import com.maksim.auth_service.exception.AuthException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
    @Value("${auth.jwt.secret}")
    private String SECRET;
    @Value("${auth.jwt.expiration}")
    private int JWT_EXPIRATION_MINUTES;

    public ValidateResponseDto validate(String token) {
        try {
            var claims = Jwts.parserBuilder().setSigningKey(getSignKey()).build()
                    .parseClaimsJws(token).getBody();
            return new ValidateResponseDto(Integer.valueOf(claims.getSubject()), claims.get("handle", String.class));
        } catch (Exception ex) {
            throw new AuthException("Invalid token");
        }
    }

    public String generateToken(String handle, int userId) {
        return Jwts.builder()
                .claim("handle", handle)
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (long) JWT_EXPIRATION_MINUTES * 60 * 10000))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        var bytes = SECRET.getBytes();
        return Keys.hmacShaKeyFor(bytes);
    }

}
