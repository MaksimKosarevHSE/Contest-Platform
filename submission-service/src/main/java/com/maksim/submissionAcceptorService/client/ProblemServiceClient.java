package com.maksim.submissionAcceptorService.client;

import com.google.protobuf.Int32Value;
import com.google.protobuf.util.Timestamps;
import com.maksim.common.dto.problem.ProblemConstrainsResponseDto;
import com.maksim.rpc.ProblemConstraintsRequest;
import com.maksim.rpc.ProblemConstraintsResponse;
import com.maksim.rpc.ProblemRpcServiceGrpc;
import com.maksim.submissionAcceptorService.exception.ResourceNotFoundException;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class ProblemServiceClient {

    private final ProblemRpcServiceGrpc.ProblemRpcServiceBlockingStub problemRpcServiceBlockingStub;


    public ProblemConstrainsResponseDto getProblemConstraints(Integer problemId, Integer contestId) {
        try {
            ProblemConstraintsRequest.Builder request = ProblemConstraintsRequest.newBuilder()
                    .setProblemId(problemId);
            if (contestId != null) {
                request.setContestId(Int32Value.of(contestId));
            }

            ProblemConstraintsResponse response = problemRpcServiceBlockingStub.getProblemConstraints(request.build());
            return new ProblemConstrainsResponseDto(
                    response.getId(),
                    response.getCompileTimeLimit(),
                    response.getTimeLimit(),
                    response.getMemoryLimit(),
                    response.hasContestId() ? response.getContestId().getValue() : null,
                    response.hasContestStartTime() ? Instant.ofEpochMilli(Timestamps.toMillis(response.getContestStartTime())) : null,
                    response.hasContestEndTime() ? Instant.ofEpochMilli(Timestamps.toMillis(response.getContestEndTime())) : null
            );
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.NOT_FOUND) {
                throw new ResourceNotFoundException("Problem with id " + problemId + " is not found");
            }
            throw new IllegalStateException("Failed to fetch problem constraints", e);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to fetch problem constraints", e);
        }
    }
}
