package com.maksim.submissionAcceptorService.service.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.maksim.submissionAcceptorService.entity.OutboxEvent;
import com.maksim.submissionAcceptorService.kafka.KafkaEventPublisher;
import com.maksim.submissionAcceptorService.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;


@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxEventService {

    private final OutboxEventRepository outboxEventRepository;

    private final KafkaEventPublisher kafkaEventPublisher;

    private final ObjectMapper objectMapper;

    @Transactional
    public void publishEvents() {
        outboxEventRepository.findAll()
                .forEach(this::process);

    }

    private void process(OutboxEvent outboxEvent) {
        try {
            kafkaEventPublisher.processOutboxEvent(outboxEvent);
            outboxEventRepository.delete(outboxEvent);
        } catch (Exception e) {
            log.error("Failed to process event {}", outboxEvent.getEventId());
        }
    }

    public void save(String topic, Object payload) {
        String payloadJson = objectMapper.writeValueAsString(payload);
        OutboxEvent event = OutboxEvent.builder()
                .eventType(topic)
                .payload(payloadJson)
                .build();
        outboxEventRepository.save(event);
    }
}