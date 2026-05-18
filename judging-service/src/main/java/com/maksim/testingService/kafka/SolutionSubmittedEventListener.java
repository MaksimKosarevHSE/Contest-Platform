package com.maksim.testingService.kafka;

import com.maksim.testingService.entity.ProcessedEvent;
import com.maksim.common.event.SolutionSubmittedEvent;
import com.maksim.testingService.service.JudgingManager;
import com.maksim.testingService.respository.ProcessedEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SolutionSubmittedEventListener {

    private final JudgingManager judgingManager;

    private final ProcessedEventRepository processedEventRepository;

    @KafkaListener(
            topics = "${solution.submitted.event.topic}",
            containerFactory = "solutionSubmittedKafkaListenerContainerFactory",
            concurrency = "2"
    )
    public void handle(@Payload SolutionSubmittedEvent solutionEvent) {
        Long submissionId = solutionEvent.getSubmissionId();
        if (submissionId == null) {
            throw new IllegalArgumentException("Solution submitted event must contain submissionId");
        }
        if (processedEventRepository.existsById(submissionId)) {
            log.info("Duplicate solution submitted event for submission {}", submissionId);
            return;
        }
        judgingManager.judge(solutionEvent);
        processedEventRepository.save(new ProcessedEvent(submissionId));
    }

}
