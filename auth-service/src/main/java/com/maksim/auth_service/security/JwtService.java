package com.maksim.auth_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtService {

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.expiration}")
    private Long expiration; // Millis

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(Integer userId, String handle) {
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("handle", handle)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public Optional<JwtTokenData> parseToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return Optional.of(
                    new JwtTokenData(Integer.valueOf(claims.getSubject()), String.valueOf(claims.get("handle", String.class)))
            );

        } catch (Exception ex) {
            return Optional.empty();
        }
    }


    public record JwtTokenData(
            Integer userId,
            String handle
    ) {
    }
}
