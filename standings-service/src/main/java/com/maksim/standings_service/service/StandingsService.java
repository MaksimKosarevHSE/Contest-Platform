package com.maksim.standings_service.service;


import com.maksim.standings_service.entity.ContestUser;
import com.maksim.standings_service.entity.ContestUserTask;
import com.maksim.standings_service.entity.Status;
import com.maksim.standings_service.entity.key.ContestUserId;
import com.maksim.standings_service.entity.key.ContestUserTaskId;
import com.maksim.standings_service.event.StandingsUpdateEvent;
import com.maksim.standings_service.repository.ContestUserRepository;
import com.maksim.standings_service.repository.ContestUserTaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class StandingsService {
    private StringRedisTemplate redisTemplate;
    private ContestUserRepository contestUserRepository;
    private ContestUserTaskRepository contestUserTaskRepository;
    private ObjectMapper om;

    public StandingsService(StringRedisTemplate redisTemplate, ContestUserRepository contestUserRepository, ContestUserTaskRepository contestUserTaskRepository, ObjectMapper om) {
        this.redisTemplate = redisTemplate;
        this.contestUserRepository = contestUserRepository;
        this.contestUserTaskRepository = contestUserTaskRepository;
        this.om = om;
    }

    public String getUserContestKey(int userId, int contestId) {
        return "contest:" + contestId + ":user:" + userId + ":tasks";
    }

    public String getContestScoresKey(int contestId) {
        return "contest:" + contestId + ":scores";
    }

    public Optional<ContestUserTask> getUserTaskProgress(int userId, int contestId, int taskId) {
        String key = getUserContestKey(userId, contestId);
        String userTaskJson = (String) redisTemplate.opsForHash().get(key, taskId);
        if (userTaskJson != null) {
            try {
                return Optional.of(om.readValue(userTaskJson, ContestUserTask.class));
            } catch (JacksonException e) {
                log.error(e.getMessage());
            }
        }
        var cutId = new ContestUserTaskId(contestId, userId, taskId);
        Optional<ContestUserTask> cut = contestUserTaskRepository.findById(cutId);
        cut.ifPresent((obj) -> {
            redisTemplate.opsForHash().put(getUserContestKey(userId, contestId), taskId, om.writeValueAsString(obj));
        });
        return cut;
    }

    public void updateUserTaskProgress(StandingsUpdateEvent event, int userId, int contestId, int taskId) {
        var cutId = new ContestUserTaskId(contestId, userId, taskId);
        Optional<ContestUserTask> cutOpt = contestUserTaskRepository.findById(cutId);
        ContestUserTask cut = cutOpt.orElseGet(() -> new ContestUserTask(cutId)); // тут скорее всего надо сразу сроу кидать
        if (cut.getIsSolved()) {
            return;
        }
        cut.setAttempts(cut.getAttempts() + 1);
        if (event.getStatus() == Status.OK) {
            cut.setIsSolved(true);
            cut.setSolutionTime(event.getSubmissionTime());
            cut.setScore(event.getScore());
        } else {
            cut.addFine(event.getFine());
        }
        contestUserTaskRepository.save(cut);

        if (event.getStatus() == Status.OK) {
            var cuId = new ContestUserId(contestId, userId);
            Optional<ContestUser> cuOpt = contestUserRepository.findById(cuId);
            ContestUser cu = cuOpt.orElseGet(() -> new ContestUser(cuId));
            cu.incrementTaskSolved();
            int delta = Integer.max(0, event.getScore() - cut.getFine());
            cu.addScore(delta);
            contestUserRepository.save(cu);

            String key = getContestScoresKey(contestId);
            redisTemplate.opsForZSet().incrementScore(key, String.valueOf(userId), delta);
            redisTemplate.expire(key, 5, TimeUnit.HOURS);
        }
        String key = getUserContestKey(userId, contestId);
        redisTemplate.opsForHash().put(key, taskId, om.writeValueAsString(cut));
        redisTemplate.expire(key, 5, TimeUnit.HOURS);
    }

    public int getScore(int userId, int contestId){
        Double score = redisTemplate.opsForZSet().score(getContestScoresKey(contestId), String.valueOf(userId));
        if (score != null) return score.intValue();
        return getContestUser(userId,contestId).getTotalScore();
        // TODO: добавить проверку на то что пользователь не решил ни одной задачи. заполнять таблицу нулями при регистрации!!!! контеста
    }

    public int getPlace(int userId, int contestId){
        Long place = redisTemplate.opsForZSet().reverseRank(getContestScoresKey(contestId), userId);
        if (place != null) return place.intValue();
        return 0;
        // TODO: пересчитывать всю таблицу......... или кинуть что подсчет таблицы недоступен, хотя n log(n) не такое уж и большое время для расчета таблицы
    }

    public List<ContestUserTask> getStandings(int page, int pageSize){
        return null;
        // TODO: рэнж запрос либо в редис или пересчитывание таблицы
    }


    public ContestUser getContestUser(int userId, int contestId){
        return contestUserRepository.findById(new ContestUserId(contestId,userId)).orElseThrow(() -> new RuntimeException("No user found"));
    }

}
