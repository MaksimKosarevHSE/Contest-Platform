package com.maksim.testingService.kafka;

import com.maksim.common.enums.Status;
import com.maksim.common.event.SolutionJudgedEvent;
import com.maksim.testingService.mapper.VerdictMapper;
import com.maksim.testingService.service.model.VerdictInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.kafka.clients.producer.ProducerRecord;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventPublisher {

    @Value("${test.case.judged.event.topic}")
    private String testCaseJudgedEventTopic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final VerdictMapper verdictMapper;

    private final Integer KAFKA_TIMEOUT_SEC = 5;

    private void sendJudgedEvent(SolutionJudgedEvent event, String eventId) {
        try {
            String key = String.valueOf(event.getSubmissionId());
            ProducerRecord<String, Object> record = new ProducerRecord<>(testCaseJudgedEventTopic, key, event);
            record.headers().add("event-id", eventId.getBytes(StandardCharsets.UTF_8));
            kafkaTemplate.send(record).get(KAFKA_TIMEOUT_SEC, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            log.error("Failed to send event to Kafka topic {}: {}", testCaseJudgedEventTopic, e.getMessage());
            throw new RuntimeException("Kafka send failed", e);
        }
    }

    public void sendProgressAsync(Long submissionId, Integer testNum) {
        SolutionJudgedEvent event = SolutionJudgedEvent.builder()
                .submissionId(submissionId)
                .testNum(testNum)
                .status(Status.TESTING).build();
        String key = String.valueOf(submissionId);
        String eventId = submissionId + ":test:" + testNum;
        ProducerRecord<String, Object> record = new ProducerRecord<>(testCaseJudgedEventTopic, key, event);
        record.headers().add("event-id", eventId.getBytes(StandardCharsets.UTF_8));
        kafkaTemplate.send(record);
    }

    public void sendVerdict(Long submissionId, VerdictInfo verdictInfo) {
        SolutionJudgedEvent event = verdictMapper.toEvent(verdictInfo);
        event.setSubmissionId(submissionId);
        sendJudgedEvent(event, submissionId + ":final");
    }
}
