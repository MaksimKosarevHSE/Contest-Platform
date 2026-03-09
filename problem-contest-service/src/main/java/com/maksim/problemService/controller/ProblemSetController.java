package com.maksim.problemService.controller;


import com.maksim.problemService.dto.problem.ProblemCreateDto;
import com.maksim.problemService.exception.ErrorResponse;
import com.maksim.problemService.service.ProblemService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProblemSetController {
    private final ProblemService problemService;

    private final int PAGE_SIZE = 20;

    public ProblemSetController(ProblemService service) {
        this.problemService = service;
    }

    @PostMapping(value = "/problem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create new problem")
    public ResponseEntity<Object> createProblem(@Valid @ModelAttribute ProblemCreateDto problemCreateDto,
                                                @RequestHeader(value = "X-User-Id", required = false) Integer userId) throws IOException {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("User is not authenticated"));
        }

        return ResponseEntity.ok(problemService.createProblem(problemCreateDto, userId));
    }


    @GetMapping("/problem/{id}")
    @Operation(summary = "Get full problem description")
    public ResponseEntity<Object> getProblemById(@PathVariable Integer id) {
        return ResponseEntity.ok(problemService.findById(id));
    }


    @GetMapping("/problem/{id}/constraints")
    @Operation(summary = "Get problem runtime limit, memory limit, compile time limit")
    public ResponseEntity<Object> getConstraintsById(@PathVariable Integer id) {
        return ResponseEntity.ok(problemService.getConstraints(id));
    }

    @GetMapping("/problem/signature")
    @Operation(summary = "Get problem signature")
    public ResponseEntity<Object> getProblemsSignatures(@RequestParam(defaultValue = "1") Integer num) {
        var page = problemService.getProblemsSignaturesPage(num, PAGE_SIZE);
        return ResponseEntity.ok(page);
    }

}
