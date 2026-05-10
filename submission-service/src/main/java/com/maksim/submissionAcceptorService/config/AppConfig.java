package com.maksim.submissionAcceptorService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;
import tools.jackson.databind.ObjectMapper;
import com.maksim.rpc.ProblemRpcServiceGrpc;

@Configuration
public class AppConfig {
    @Bean
    public ProblemRpcServiceGrpc.ProblemRpcServiceBlockingStub problemRpcServiceBlockingStub(GrpcChannelFactory channelFactory){
        return ProblemRpcServiceGrpc.newBlockingStub(channelFactory.createChannel("problem"));
    }
    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
}
