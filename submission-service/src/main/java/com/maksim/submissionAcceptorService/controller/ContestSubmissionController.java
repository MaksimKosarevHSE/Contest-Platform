package com.maksim.submissionAcceptorService.controller;

import com.maksim.submissionAcceptorService.dto.CreateSubmissionDto;
import com.maksim.submissionAcceptorService.enums.ProgrammingLanguage;
import com.maksim.submissionAcceptorService.enums.Status;
import com.maksim.submissionAcceptorService.exception.ErrorResponse;
import com.maksim.submissionAcceptorService.repository.SubmissionRepository;
import com.maksim.submissionAcceptorService.service.SubmissionService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/api/sub")
public class ContestSubmissionController {
    private final SubmissionService submissionService;

    ContestSubmissionController(SubmissionService submitSolutionService, SubmissionRepository submissionRepository) {
        this.submissionService = submitSolutionService;
    }

    @PostMapping("/contest/{contestId}/problem/{problemId}/submit")
    @Operation(summary = "Submit problem solution in contest")
    public ResponseEntity<?> submitSolution(@PathVariable Integer contestId,
                                            @PathVariable Integer problemId,
                                            @ModelAttribute CreateSubmissionDto solution,
                                            @RequestHeader(value = "X-User-Id", required = false) Integer userId
    ) throws IOException, ExecutionException, InterruptedException {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("User is not authenticated"));
        }
        long id = submissionService.submitSolution(problemId, contestId, userId, solution);
        return ResponseEntity.ok(id);
    }


    @GetMapping("/contest/{contestId}/submissions")
    public ResponseEntity<?> getSubmissions(@PathVariable Integer contestId,
                                            @RequestParam(required = false) Integer problemId,
                                            @RequestParam(required = false) Integer userId,
                                            @RequestParam(required = false) Status status,
                                            @RequestParam(required = false) ProgrammingLanguage language,
                                            @RequestParam(defaultValue = "1") Integer page) {
        var res = submissionService.getSubmissions(contestId, problemId, userId, status, language, page);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/contest/{contestId}/submission/{submissionId}/details")
    public ResponseEntity<Object> getSubmissionDetails(@PathVariable Long submissionId,
                                                       @PathVariable Integer contestId,
                                                       @RequestHeader(value = "X-User-Id", required = false) Integer userId) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("User is not authenticated"));
        }
        var result = submissionService.getSubmissionDetails(submissionId, contestId, userId);
        return ResponseEntity.ok(result);
    }

}

