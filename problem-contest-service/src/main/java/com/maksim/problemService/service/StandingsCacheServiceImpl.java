package com.maksim.problemService.service;

import com.maksim.problemService.dto.standings.TaskProgressResponseDto;
import com.maksim.problemService.dto.standings.UserProgressResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StandingsCacheServiceImpl implements StandingsCacheService {

    private static final String LEADERBOARD_PREFIX = "contest:leaderboard:";

    private static final String USER_DETAILS_PREFIX = "contest:details:";

    private static final String CACHE_READY_PREFIX = "contest:leaderboard-ready:";

    private final StringRedisTemplate redisTemplate;

    private final ObjectMapper objectMapper;

    private String leaderboardKey(int contestId) {
        return LEADERBOARD_PREFIX + contestId;
    }

    private String cacheReadyKey(int contestId) {
        return CACHE_READY_PREFIX + contestId;
    }

    private String userDetailsKey(int contestId, int userId) {
        return USER_DETAILS_PREFIX + contestId + ":user:" + userId;
    }

    private String userDetailsPattern(int contestId) {
        return USER_DETAILS_PREFIX + contestId + ":user:*";
    }

    public boolean existsLeaderboard(int contestId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(cacheReadyKey(contestId)));
    }

    public void putLeaderboardScore(int contestId, int userId, int totalScore) {
        redisTemplate.opsForZSet().add(leaderboardKey(contestId), String.valueOf(userId), totalScore);
    }

    public Integer getUserRank(int contestId, int userId) {
        Long rank = redisTemplate.opsForZSet().reverseRank(leaderboardKey(contestId), String.valueOf(userId));
        if (rank == null) {
            return null;
        }
        return rank.intValue() + 1;
    }

    public Integer getUserScore(int contestId, int userId) {
        Double score = redisTemplate.opsForZSet().score(leaderboardKey(contestId), String.valueOf(userId));
        return score != null ? score.intValue() : null;
    }

    public Set<ZSetOperations.TypedTuple<String>> getLeaderboardRange(int contestId, long start, long end) {
        return redisTemplate.opsForZSet().reverseRangeWithScores(leaderboardKey(contestId), start, end);
    }

    public Long getLeaderboardTotalSize(int contestId) {
        return redisTemplate.opsForZSet().size(leaderboardKey(contestId));
    }

    public void putUserTaskDetail(int contestId, int userId, int taskId, TaskProgressResponseDto taskDetail) {
        String taskJson = objectMapper.writeValueAsString(taskDetail);
        redisTemplate.opsForHash().put(userDetailsKey(contestId, userId), String.valueOf(taskId), taskJson);
    }

    public Map<Integer, TaskProgressResponseDto> getUserTasksDetails(int contestId, int userId) {
        String key = userDetailsKey(contestId, userId);
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
        if (entries.isEmpty()) return Collections.emptyMap();

        Map<Integer, TaskProgressResponseDto> result = new HashMap<>();
        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            int taskId = Integer.parseInt(entry.getKey().toString());
            TaskProgressResponseDto dto = objectMapper.readValue(entry.getValue().toString(), TaskProgressResponseDto.class);
            result.put(taskId, dto);
        }
        return result;
    }

    private void deleteLeaderboard(int contestId) {
        redisTemplate.delete(leaderboardKey(contestId));
    }

    private void evictContest(int contestId) {
        redisTemplate.delete(cacheReadyKey(contestId));
        deleteLeaderboard(contestId);
        deleteUserDetails(contestId);
    }

    public void rebuildFromDatabase(int contestId, List<UserProgressResponseDto> users) {
        String leaderboardKey = leaderboardKey(contestId);
        evictContest(contestId);

        for (UserProgressResponseDto user : users) {
            redisTemplate.opsForZSet().add(leaderboardKey, String.valueOf(user.userId()), user.score());
            String userKey = userDetailsKey(contestId, user.userId());

            Map<String, String> taskMap = user.taskProgress().stream()
                    .collect(Collectors.toMap(
                            t -> String.valueOf(t.taskId()),
                            objectMapper::writeValueAsString
                    ));
            if (!taskMap.isEmpty()) {
                redisTemplate.opsForHash().putAll(userKey, taskMap);
            }
        }
        redisTemplate.opsForValue().set(cacheReadyKey(contestId), "1");
    }

    private void deleteUserDetails(int contestId) {
        Set<String> keys = redisTemplate.keys(userDetailsPattern(contestId));
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
}
