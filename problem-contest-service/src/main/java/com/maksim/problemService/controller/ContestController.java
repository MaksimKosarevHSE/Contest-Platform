package com.maksim.problemService.controller;

import com.maksim.problemService.dto.contest.ContestSignatureResponseDto;
import com.maksim.problemService.dto.contest.CreateContestDto;
import com.maksim.problemService.dto.problem.ProblemSignatureResponseDto;
import com.maksim.problemService.entity.Problem;
import com.maksim.problemService.entity.ProblemConstraints;
import com.maksim.problemService.exception.ErrorResponse;
import com.maksim.problemService.service.ContestService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@CrossOrigin
@RequestMapping("/api")
public class ContestController {
    private final ContestService contestService;

    private final Integer PAGE_SIZE = 20;

    public ContestController(ContestService contestService) {
        this.contestService = contestService;
    }

    @PostMapping("/contest/{contestId}/contestants")
    @Operation(summary = "Register on contest")
    public ResponseEntity<?> registerOnContest(@PathVariable Integer contestId,
                                               @RequestHeader(value = "X-User-Id", required = false) Integer userId) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("User is not authenticated"));
        }
        contestService.registerUser(contestId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/contest/{contestId}/contestants")
    @Operation(summary = "Get contestants of contests")
    public ResponseEntity<?> getContestantsList(@PathVariable Integer contestId) {
        return ResponseEntity.ok(List.of("Vasya", "Dima"));
    }

    @GetMapping("/contest/{contestId}/problems")
    @Operation(summary = "Get signatures of contest's problems")
    public ResponseEntity<Object> getSignatures(@PathVariable Integer contestId) {
        List<ProblemSignatureResponseDto> constraints = contestService.getAllProblemSignatures(contestId);
        return ResponseEntity.ok(constraints);
    }


    @GetMapping("/contest/{contestId}/problem/{problemId}")
    @Operation(summary = "Get full problem description")
    public ResponseEntity<Object> getProblemById(@PathVariable Integer contestId, @PathVariable Integer problemId) {
        Problem problem = contestService.getProblem(contestId, problemId);
        return ResponseEntity.ok(problem);
    }


    @GetMapping("/contests")
    @Operation(summary = "Get page of contests")
    public ResponseEntity<Object> getAllPublicContests(@RequestParam(defaultValue = "1") Integer page, HttpServletRequest req) {
        Page<ContestSignatureResponseDto> pageResult = contestService.getPublicContests(page, PAGE_SIZE);
        return ResponseEntity.ok(pageResult);
    }

    @GetMapping("/contests/my")
    @Operation(summary = "Get history of participation in contests")
    public ResponseEntity<Object> getUserContests(@RequestParam(defaultValue = "1") Integer page,
                                                  @RequestHeader(value = "X-User-Id", required = false) Integer userId) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("User is not authenticated"));
        }
        Page<ContestSignatureResponseDto> list = contestService.getUserContests(userId, page, PAGE_SIZE);
        return ResponseEntity.ok(list);
    }

    @GetMapping("contest/{contestId}/problem/{problemId}/constraints")
    @Operation(summary = "Get problem's runtime limit, memory limit, compile time limit")
    public ResponseEntity<Object> getConstraintsById(@PathVariable Integer contestId, @PathVariable Integer problemId) {
        ProblemConstraints constraints = contestService.getConstraints(contestId, problemId);
        return ResponseEntity.ok(constraints);
    }


    @PostMapping("/contest/create")
    @Operation(summary = "Create new contest")
    public ResponseEntity<Object> createContest(@Valid @RequestBody CreateContestDto dto,
                                                @RequestHeader(value = "X-User-Id", required = false) Integer userId) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("User is not authenticated"));
        }
        int id = contestService.createContest(dto, userId);
        return ResponseEntity.ok(id);
    }

}

