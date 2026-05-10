package com.maksim.problemService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;
import tools.jackson.databind.ObjectMapper;
import com.maksim.rpc.JudgingRpcServiceGrpc;

@Configuration
public class AppConfig {
    @Bean
    public JudgingRpcServiceGrpc.JudgingRpcServiceBlockingStub judgingRpcServiceBlockingStub(GrpcChannelFactory channelFactory) {
        return JudgingRpcServiceGrpc.newBlockingStub(channelFactory.createChannel("judging"));
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
