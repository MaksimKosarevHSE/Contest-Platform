package com.maksim.standings_service.controller;


import com.maksim.standings_service.service.StandingsService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.ObjectMapper;

@RestController
public class StandingsController {
    private final StandingsService standingsService;
    private final RestTemplate restTemplate;
    private final ObjectMapper om;
    private final Integer PAGE_SIZE = 5;
    public StandingsController(StandingsService standingsService, RestTemplate rt, ObjectMapper om) {
        this.standingsService = standingsService;
        this.restTemplate = rt;
        this.om = om;
    }


    @GetMapping("/api/contest/{contestId}/standings")
    public ResponseEntity<Object> getStandings(@PathVariable Integer contestId, @RequestParam(name = "page", defaultValue = "0") Integer page){
        var result = standingsService.getStandings(page, PAGE_SIZE);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/api/contest/{contestId}/user/{userId}/progress")
    public ResponseEntity<Object> getUserProgress(@PathVariable Integer contestId, @PathVariable Integer userId){
        var result = standingsService.getProgress(contestId, userId);
    }

}
