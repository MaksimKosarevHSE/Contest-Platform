package com.maksim.problemService.controller;

import com.maksim.problemService.exception.ErrorResponse;
import com.maksim.problemService.service.StandingsService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
public class StandingsController {
    private final StandingsService standingsService;

    private final int PAGE_SIZE = 10;

    public StandingsController(StandingsService standingsService) {
        this.standingsService = standingsService;
    }


    @GetMapping("/contest/{contestId}/standings")
    @Operation(summary = "Get contest's standings")
    public ResponseEntity<Object> getStandings(@PathVariable Integer contestId,
                                               @RequestParam(name = "page", defaultValue = "1") Integer page) {
        var result = standingsService.getLeaderboard(contestId, page, PAGE_SIZE);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/contest/{contestId}/standings/me")
    @Operation(summary = "Get your position in contest with details")
    public ResponseEntity<Object> getUserProgress(@PathVariable Integer contestId,
                                                  @RequestHeader(value = "X-User-Id", required = false) Integer userId) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("User is not authenticated"));
        }
        return ResponseEntity.ok(standingsService.getUserProgressDto(contestId, userId));
    }
}
