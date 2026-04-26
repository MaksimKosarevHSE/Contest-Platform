package com.maksim.problemService.client;

import com.maksim.problemService.dto.problem.SendTestCasesToJudgeServiceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class JudgingServiceClient {

    @Value("${test.service.url}")
    private String testServiceUrl;

    private final RestTemplate restTemplate;

    public void saveTestCases(SendTestCasesToJudgeServiceDto dto) {
        ResponseEntity<String> response = restTemplate
                .postForEntity(testServiceUrl + "/append-tests", dto, String.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            log.error("Error saving test cases (problem ID: {})", dto.getProblemId());
            throw new RuntimeException(response.getBody());
        }
    }
}
