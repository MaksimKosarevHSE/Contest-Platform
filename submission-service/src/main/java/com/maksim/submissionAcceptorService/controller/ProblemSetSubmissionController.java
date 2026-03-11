package com.maksim.submissionAcceptorService.controller;

import com.maksim.submissionAcceptorService.dto.CreateSubmissionDto;
import com.maksim.submissionAcceptorService.enums.ProgrammingLanguage;
import com.maksim.submissionAcceptorService.enums.Status;
import com.maksim.submissionAcceptorService.exception.ErrorResponse;
import com.maksim.submissionAcceptorService.service.SubmissionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.ExecutionException;


@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/api/submissions")
public class ProblemSetSubmissionController {
    private final SubmissionService submissionService;

    ProblemSetSubmissionController(SubmissionService submitSolutionService) {
        this.submissionService = submitSolutionService;
    }

    @PostMapping("/problemset/{problemId}/submit")
    @Operation(summary = "Submit solution on problem from problem set")
    public ResponseEntity<?> submitSolution(@PathVariable Integer problemId,
                                                 @ModelAttribute CreateSubmissionDto solution,
                                                 @RequestHeader(value = "X-User-Id", required = false) Integer userId
    ) throws IOException, ExecutionException, InterruptedException {

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("User is not authenticated"));
        }

        long id = submissionService.submitSolution(problemId, null, userId, solution);
        return ResponseEntity.ok(id);
    }


    @GetMapping("/problemset/submissions")
    public ResponseEntity<?> getSubmissions(@RequestParam(required = false) Integer problemId,
                                                 @RequestParam(required = false) Integer userId,
                                                 @RequestParam(required = false) Status status,
                                                 @RequestParam(required = false) ProgrammingLanguage language,
                                                 @RequestParam(defaultValue = "1") Integer page) {

        var result = submissionService.getSubmissions(null, problemId, userId, status, language, page);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/problemset/submission/{submissionId}/details")
    public ResponseEntity<?> getSubmissionDetails(@PathVariable Long submissionId,
                                                       @RequestHeader(value = "X-User-Id", required = false) Integer userId) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("User is not authenticated"));
        }
        var result = submissionService.getSubmissionDetails(submissionId, null, userId);
        return ResponseEntity.ok(result);
    }
}
