package com.maksim.problemService.kafka;

import com.maksim.common.event.StandingsUpdateEvent;
import com.maksim.problemService.entity.ProcessedEvent;
import com.maksim.problemService.repository.ProcessedEventRepository;
import com.maksim.problemService.service.StandingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class StandingsEventListener {

    private final StandingsService standingsService;

    private final ProcessedEventRepository processedEventRepository;

    @KafkaListener(
            topics = "${standings.update.event.topic}",
            containerFactory = "standingsUpdateKafkaListenerContainerFactory"
    )
    @Transactional
    public void handleStandingsUpdate(@Payload StandingsUpdateEvent event) {
        Long submissionId = event.getSubmissionId();
        if (submissionId == null) {
            throw new IllegalArgumentException("Standings update event must contain submissionId");
        }
        if (processedEventRepository.existsById(submissionId)) {
            log.info("Duplicate standings update event for submission {}", submissionId);
            return;
        }
        standingsService.handleUpdateEvent(event);
        processedEventRepository.save(new ProcessedEvent(submissionId));
    }
}
