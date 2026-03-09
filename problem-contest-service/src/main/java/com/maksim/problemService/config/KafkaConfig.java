package com.maksim.problemService.config;

import com.maksim.problemService.dto.problem.SendTestCasesToJudgeServiceDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {
    @Value("${consumer.group_id}")
    private String GROUP_ID;
    @Value("${spring.kafka.bootstrap-servers}")
    private String KAFKA_BOOTSTRAP;

    @Bean
    ConsumerFactory<Integer, SendTestCasesToJudgeServiceDto> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BOOTSTRAP);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        props.put(JacksonJsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JacksonJsonDeserializer.class);
        props.put(JacksonJsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        return new DefaultKafkaConsumerFactory<>(props, new IntegerDeserializer(), new JacksonJsonDeserializer<>(SendTestCasesToJudgeServiceDto.class));
    }

    @Bean("factory1")
    public ConcurrentKafkaListenerContainerFactory<Integer, SendTestCasesToJudgeServiceDto> concurrentKafkaListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<Integer, SendTestCasesToJudgeServiceDto>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
