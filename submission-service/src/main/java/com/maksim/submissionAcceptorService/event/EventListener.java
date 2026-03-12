package com.maksim.submissionAcceptorService.event;

import com.maksim.submissionAcceptorService.enums.Status;
import com.maksim.submissionAcceptorService.service.SubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventListener {
    private final SubmissionService service;

    @KafkaListener(topics = "test-case-judged-event-topic", containerFactory = "factory1")
    public void handle(@Payload SolutionJudgedEvent solutionEvent){
        if (solutionEvent.getStatus() == Status.TESTING){
            service.updateSolutionStatus(solutionEvent);
        } else {
            service.processJudgedSolution(solutionEvent);
        }
    }
}
