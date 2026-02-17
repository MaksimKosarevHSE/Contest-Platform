package com.maksim.standings_service.handler;


import com.maksim.standings_service.entity.ContestUser;
import com.maksim.standings_service.entity.ContestUserTask;
import com.maksim.standings_service.entity.Status;
import com.maksim.standings_service.entity.key.ContestUserId;
import com.maksim.standings_service.entity.key.ContestUserTaskId;
import com.maksim.standings_service.event.StandingsUpdateEvent;
import com.maksim.standings_service.repository.ContestUserRepository;
import com.maksim.standings_service.repository.ContestUserTaskRepository;
import org.apache.kafka.common.metrics.Stat;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class StandingsUpdateService {
    private StringRedisTemplate redisTemplate;
    private ContestUserRepository contestUserRepository;
    private ContestUserTaskRepository contestUserTaskRepository;

    public StandingsUpdateService(StringRedisTemplate redisTemplate, ContestUserRepository contestUserRepository, ContestUserTaskRepository contestUserTaskRepository) {
        this.redisTemplate = redisTemplate;
        this.contestUserRepository = contestUserRepository;
        this.contestUserTaskRepository = contestUserTaskRepository;
    }

    @KafkaListener(topics = "standings-update-event")
    private void standingsUpdateEventHandler(@Payload StandingsUpdateEvent event) {

    }

}
