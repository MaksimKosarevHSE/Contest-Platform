package com.maksim.submissionAcceptorService.kafka;

import com.maksim.submissionAcceptorService.entity.OutboxEvent;
import com.maksim.common.event.SolutionSubmittedEvent;
import com.maksim.common.event.StandingsUpdateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final ObjectMapper om;

    @Value("${outbox.kafka.timeout:5}")
    private int kafkaTimeout;

    @Value("${solution.submitted.event.topic}")
    private String solutionSubmittedTopicName;

    @Value("${standings.update.event.topic}")
    private String standingsUpdateTopicName;

    public void processOutboxEvent(OutboxEvent outboxEvent) {
        Object event = deserializePayload(outboxEvent);
        try {
            ProducerRecord<String, Object> record = new ProducerRecord<>(outboxEvent.getEventType(), createKey(event), event);
            record.headers().add("event-id", outboxEvent.getEventId().toString().getBytes(StandardCharsets.UTF_8));
            kafkaTemplate.send(record).get(kafkaTimeout, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            log.error("Failed to send event to Kafka topic {}: {}", outboxEvent.getEventType(), e.getMessage());
            throw new RuntimeException("Kafka sending failed", e);
        }
    }

    private Object deserializePayload(OutboxEvent outboxEvent) {
        if (solutionSubmittedTopicName.equals(outboxEvent.getEventType())) {
            return om.readValue(outboxEvent.getPayload(), SolutionSubmittedEvent.class);
        }
        if (standingsUpdateTopicName.equals(outboxEvent.getEventType())) {
            return om.readValue(outboxEvent.getPayload(), StandingsUpdateEvent.class);
        }
        throw new IllegalArgumentException("There is no such event type: " + outboxEvent.getEventType());
    }

    private String createKey(Object event) {
        if (event instanceof SolutionSubmittedEvent submittedEvent) {
            return String.valueOf(submittedEvent.getSubmissionId());
        }
        if (event instanceof StandingsUpdateEvent standingsEvent) {
            return String.valueOf(standingsEvent.getSubmissionId());
        }
        return null;
    }
}
