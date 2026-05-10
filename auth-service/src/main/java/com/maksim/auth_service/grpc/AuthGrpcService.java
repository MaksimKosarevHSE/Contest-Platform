package com.maksim.auth_service.grpc;

import com.maksim.auth_service.dto.ValidateRequest;
import com.maksim.auth_service.dto.ValidateResponse;
import com.maksim.auth_service.exception.UnauthorizedException;
import com.maksim.auth_service.service.AuthService;
import com.maksim.rpc.AuthRpcServiceGrpc;
import com.maksim.rpc.ValidateTokenRequest;
import com.maksim.rpc.ValidateTokenResponse;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthGrpcService extends AuthRpcServiceGrpc.AuthRpcServiceImplBase {

    private final AuthService authService;

    @Override
    public void validateToken(ValidateTokenRequest request, StreamObserver<ValidateTokenResponse> responseObserver) {
        try {
            ValidateResponse response = authService.validate(new ValidateRequest(request.getToken()));
            responseObserver.onNext(ValidateTokenResponse.newBuilder()
                    .setUserId(response.id())
                    .setHandle(response.handle())
                    .build());
            responseObserver.onCompleted();
        } catch (UnauthorizedException ex) {
            responseObserver.onError(Status.UNAUTHENTICATED
                    .withDescription(ex.getMessage())
                    .asRuntimeException());
        } catch (Exception ex) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Failed to validate token")
                    .asRuntimeException());
        }
    }
}
