package com.maksim.testingService.grpc;

import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import com.maksim.common.dto.problem.SaveTestCasesRequestDto;
import com.maksim.common.enums.CheckerType;
import com.maksim.common.enums.ProgrammingLanguage;
import com.maksim.rpc.CheckerTypeRpc;
import com.maksim.rpc.JudgingRpcServiceGrpc;
import com.maksim.rpc.ProgrammingLanguageRpc;
import com.maksim.rpc.SaveTestCasesRequest;
import com.maksim.testingService.exception.JuryCompilationException;
import com.maksim.testingService.service.JudgingService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JudgingGrpcService extends JudgingRpcServiceGrpc.JudgingRpcServiceImplBase {

    private final JudgingService judgingService;

    @Override
    public void saveTestCases(SaveTestCasesRequest request, StreamObserver<Empty> responseObserver) {
        try {
            judgingService.saveTests(new SaveTestCasesRequestDto(
                    request.getProblemId(),
                    request.getTestFilesContentList().stream().map(ByteString::toByteArray).toList(),
                    List.copyOf(request.getTestFilesNamesList()),
                    request.getCountOfTestCases(),
                    mapCheckerType(request.getCheckerType()),
                    mapProgrammingLanguage(request.getCheckerLanguage()),
                    request.getCheckerSourceCode().isEmpty() ? null : request.getCheckerSourceCode().toByteArray()
            ));
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (JuryCompilationException ex) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription(ex.getMessage())
                    .asRuntimeException());
        } catch (Exception ex) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Failed to save test cases")
                    .asRuntimeException());
        }
    }

    private CheckerType mapCheckerType(CheckerTypeRpc checkerType) {
        return switch (checkerType) {
            case DEFAULT_EXACT_MATCH_CHECKER -> CheckerType.DEFAULT_EXACT_MATCH_CHECKER;
            case CUSTOM_CHECKER -> CheckerType.CUSTOM_CHECKER;
            default -> throw new IllegalArgumentException("Unsupported checker type: " + checkerType);
        };
    }

    private ProgrammingLanguage mapProgrammingLanguage(ProgrammingLanguageRpc language) {
        return switch (language) {
            case JAVA -> ProgrammingLanguage.Java;
            case CPP -> ProgrammingLanguage.Cpp;
            case PROGRAMMING_LANGUAGE_RPC_UNSPECIFIED -> null;
            default -> throw new IllegalArgumentException("Unsupported programming language: " + language);
        };
    }
}
