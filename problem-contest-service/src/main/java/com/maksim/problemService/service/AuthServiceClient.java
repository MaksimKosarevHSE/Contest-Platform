package com.maksim.problemService.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.Serial;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class AuthServiceClient {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${auth.service.url}")
    private String authServiceUrl;

    Map<Integer, String> getUsersHandles(List<Integer> ids) {
        String url = authServiceUrl + "/api/users/handles?ids=" +
                ids.stream().map(String::valueOf).collect(Collectors.joining(","));
        ResponseEntity<Map<Integer, String>> response = restTemplate
                .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });
        return response.getBody();
    }

    String getUserHandle(Integer id) {
        String url = authServiceUrl + "/api/users/" + id + "/handle";
        return restTemplate.getForObject(url, String.class);
    }
}
