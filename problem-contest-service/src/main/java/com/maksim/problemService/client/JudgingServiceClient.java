package com.maksim.problemService.client;

import com.google.protobuf.ByteString;
import com.maksim.common.dto.problem.SaveTestCasesRequestDto;
import com.maksim.common.enums.CheckerType;
import com.maksim.common.enums.ProgrammingLanguage;
import com.maksim.rpc.CheckerTypeRpc;
import com.maksim.rpc.JudgingRpcServiceGrpc;
import com.maksim.rpc.ProgrammingLanguageRpc;
import com.maksim.rpc.SaveTestCasesRequest;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JudgingServiceClient {

    private final JudgingRpcServiceGrpc.JudgingRpcServiceBlockingStub judgingRpcServiceBlockingStub;

    public void saveTestCases(SaveTestCasesRequestDto dto) {
        try {
            judgingRpcServiceBlockingStub.saveTestCases(SaveTestCasesRequest.newBuilder()
                    .setProblemId(dto.problemId())
                    .addAllTestFilesContent(dto.testFilesContent().stream().map(ByteString::copyFrom).toList())
                    .addAllTestFilesNames(dto.testFilesNames())
                    .setCountOfTestCases(dto.countOfTestCases())
                    .setCheckerType(mapCheckerType(dto.checkerType()))
                    .setCheckerLanguage(mapProgrammingLanguage(dto.checkerLanguage()))
                    .setCheckerSourceCode(dto.checkerSourceCode() == null
                            ? ByteString.EMPTY
                            : ByteString.copyFrom(dto.checkerSourceCode()))
                    .build());
        } catch (StatusRuntimeException ex) {
            log.error("Error saving test cases. Problem ID: {})", dto.problemId(), ex);
            if (ex.getStatus().getCode() == Status.Code.INVALID_ARGUMENT) {
                throw new IllegalArgumentException(ex.getStatus().getDescription(), ex);
            }
            throw new IllegalStateException("Failed to save test cases", ex);
        }
    }

    private CheckerTypeRpc mapCheckerType(CheckerType checkerType) {
        return switch (checkerType) {
            case DEFAULT_EXACT_MATCH_CHECKER -> CheckerTypeRpc.DEFAULT_EXACT_MATCH_CHECKER;
            case CUSTOM_CHECKER -> CheckerTypeRpc.CUSTOM_CHECKER;
        };
    }

    private ProgrammingLanguageRpc mapProgrammingLanguage(ProgrammingLanguage language) {
        if (language == null) {
            return ProgrammingLanguageRpc.PROGRAMMING_LANGUAGE_RPC_UNSPECIFIED;
        }
        return switch (language) {
            case Java -> ProgrammingLanguageRpc.JAVA;
            case Cpp -> ProgrammingLanguageRpc.CPP;
        };
    }
}
