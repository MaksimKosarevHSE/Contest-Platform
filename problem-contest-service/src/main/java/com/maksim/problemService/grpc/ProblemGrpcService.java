package com.maksim.problemService.grpc;

import com.google.protobuf.Int32Value;
import com.google.protobuf.Timestamp;
import com.google.protobuf.util.Timestamps;
import com.maksim.common.dto.problem.ProblemConstrainsResponseDto;
import com.maksim.problemService.exception.ResourceNotFoundException;
import com.maksim.problemService.service.ProblemService;
import com.maksim.rpc.ProblemConstraintsRequest;
import com.maksim.rpc.ProblemConstraintsResponse;
import com.maksim.rpc.ProblemRpcServiceGrpc;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ProblemGrpcService extends ProblemRpcServiceGrpc.ProblemRpcServiceImplBase {

    private final ProblemService problemService;

    @Override
    public void getProblemConstraints(ProblemConstraintsRequest request,
                                      StreamObserver<ProblemConstraintsResponse> responseObserver) {
        try {
            Integer contestId = request.hasContestId() ? request.getContestId().getValue() : null;
            ProblemConstrainsResponseDto dto = problemService.getProblemConstraints(contestId, request.getProblemId());

            ProblemConstraintsResponse.Builder response = ProblemConstraintsResponse.newBuilder()
                    .setId(dto.getId())
                    .setCompileTimeLimit(dto.getCompileTimeLimit())
                    .setTimeLimit(dto.getTimeLimit())
                    .setMemoryLimit(dto.getMemoryLimit());

            if (dto.getContestId() != null) {
                response.setContestId(Int32Value.of(dto.getContestId()));
            }
            if (dto.getContestStartTime() != null) {
                response.setContestStartTime(toTimestamp(dto.getContestStartTime()));
            }
            if (dto.getContestEndTime() != null) {
                response.setContestEndTime(toTimestamp(dto.getContestEndTime()));
            }

            responseObserver.onNext(response.build());
            responseObserver.onCompleted();
        } catch (ResourceNotFoundException ex) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription(ex.getMessage())
                    .asRuntimeException());
        } catch (Exception ex) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Failed to fetch problem constraints")
                    .asRuntimeException());
        }
    }

    private Timestamp toTimestamp(Instant instant) {
        return Timestamps.fromMillis(instant.toEpochMilli());
    }
}
