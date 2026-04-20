package com.maksim.auth_service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Value("${security.refresh.expiration}")
    private Long refreshExpiration;

    private static final String REFRESH_PREFIX = "refresh:";

    private final StringRedisTemplate redisTemplate;

    public UUID create(Integer userId, String handle) {
        UUID token = UUID.randomUUID();
        String key = REFRESH_PREFIX + token;
        Map<String, String> fields = new HashMap<>();
        fields.put("userId", userId.toString());
        fields.put("handle", handle);

        redisTemplate.execute(new SessionCallback<>() {
            @Override
            @SuppressWarnings("unchecked")
            public Object execute(RedisOperations ops) {
                ops.multi();
                try {
                    ops.opsForHash().putAll(key, fields);
                    ops.expire(key, refreshExpiration, TimeUnit.MILLISECONDS);
                    return ops.exec();
                } catch (Exception ex) {
                    ops.discard();
                    throw ex;
                }
            }
        });

        return token;
    }

    public Optional<RefreshTokenData> getTokenData(UUID token) {
        String key = REFRESH_PREFIX + token;
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
        if (entries.isEmpty()) {
            return Optional.empty();
        }
        String userIdStr = (String) entries.get("userId");
        String handle = (String) entries.get("handle");
        if (userIdStr == null || handle == null) {
            return Optional.empty();
        }
        return Optional.of(new RefreshTokenData(Integer.valueOf(userIdStr), handle));
    }

    public void delete(UUID refreshToken) {
        String key = REFRESH_PREFIX + refreshToken;
        redisTemplate.delete(key);
    }

    public record RefreshTokenData(
        Integer userId,
        String handle
    ){
    }
}
