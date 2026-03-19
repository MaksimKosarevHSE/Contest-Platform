package com.maksim.problemService.controller;

import com.maksim.problemService.exception.ErrorResponse;
import com.maksim.problemService.service.StandingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
@CrossOrigin
public class StandingsController {
    private final StandingsService standingsService;

    private final int PAGE_SIZE = 10;

    public StandingsController(StandingsService standingsService) {
        this.standingsService = standingsService;
    }


    @GetMapping("/contest/{contestId}/standings")
    @Operation(summary = "Get contest's standings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Submission was accepted. Returns ID",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Object> getStandings(@PathVariable Integer contestId,
                                               @RequestParam(name = "page", defaultValue = "1") Integer page) {
        var result = standingsService.getLeaderboard(contestId, page, PAGE_SIZE);
        return ResponseEntity.ok(result);
    }
}
