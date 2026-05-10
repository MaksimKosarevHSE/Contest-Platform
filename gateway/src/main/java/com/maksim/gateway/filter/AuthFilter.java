package com.maksim.gateway.filter;

import com.maksim.rpc.AuthRpcServiceGrpc;
import com.maksim.rpc.ValidateTokenRequest;
import com.maksim.rpc.ValidateTokenResponse;
import io.grpc.StatusRuntimeException;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;

@Component
public class AuthFilter implements GlobalFilter, Ordered {

    private final AuthRpcServiceGrpc.AuthRpcServiceBlockingStub authRpcServiceBlockingStub;

    public AuthFilter(AuthRpcServiceGrpc.AuthRpcServiceBlockingStub authRpcServiceBlockingStub) {
        this.authRpcServiceBlockingStub = authRpcServiceBlockingStub;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest().mutate()
                .headers(headers -> {
                    headers.remove("X-User-Id");
                    headers.remove("X-User-Handle");
                })
                .build();

        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        String token = authHeader.substring(7);

        return validateToken(token)
                .flatMap(response -> {
                    ServerHttpRequest mutatedRequest = request.mutate()
                            .header("X-User-Id", Integer.toString(response.getUserId()))
                            .header("X-User-Handle", response.getHandle())
                            .build();
                    ServerWebExchange mutatedExchange = exchange.mutate()
                            .request(mutatedRequest)
                            .build();
                    return chain.filter(mutatedExchange);
                })
                .onErrorResume(e -> onError(exchange));
    }

    private Mono<ValidateTokenResponse> validateToken(String token) {
        return Mono.fromCallable(() -> authRpcServiceBlockingStub.validateToken(
                        ValidateTokenRequest.newBuilder()
                                .setToken(token)
                                .build()))
                .subscribeOn(Schedulers.boundedElastic())
                .onErrorMap(StatusRuntimeException.class, ex -> new RuntimeException(ex));
    }

    private Mono<Void> onError(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        DataBuffer buffer = response.bufferFactory()
                .wrap(("{\"message\":\"Invalid token\"}").getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return 10000;
    }
}