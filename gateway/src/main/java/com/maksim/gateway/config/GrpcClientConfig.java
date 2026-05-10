package com.maksim.gateway.config;

import com.maksim.rpc.AuthRpcServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

@Configuration
public class GrpcClientConfig {

    @Bean
    public AuthRpcServiceGrpc.AuthRpcServiceBlockingStub authRpcServiceBlockingStub(GrpcChannelFactory channelFactory) {
        return AuthRpcServiceGrpc.newBlockingStub(channelFactory.createChannel("auth"));
    }
}
