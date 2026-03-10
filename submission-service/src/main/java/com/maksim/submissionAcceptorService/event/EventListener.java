package com.maksim.submissionAcceptorService.event;

import com.maksim.submissionAcceptorService.service.SubmissionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class EventListener {
    private final SubmissionService service;

    @KafkaListener(topics = "solution-judged-event-topic", containerFactory = "factory1")
    public void handle(@Payload SolutionJudgedEvent solutionEvent){
        service.processJudgedSolution(solutionEvent);
        // РАЗОБРАТЬСЯ С ТРАНЗАКЦИЯМИ
    }
}
