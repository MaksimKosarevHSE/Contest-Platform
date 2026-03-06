package com.maksim.problemService.handler;

import com.maksim.standings_service.event.StandingsUpdateEvent;
import com.maksim.standings_service.service.StandingsService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class StandingsUpdateService {
    private final StandingsService standingsService;
    public StandingsUpdateService(StandingsService sc) {
        this.standingsService = sc;
    }

    @KafkaListener(topics = "standings-update-event")
    private void standingsUpdateEventHandler(@Payload StandingsUpdateEvent event) {
        standingsService.updateUserTaskProgress(event);
    }

}
