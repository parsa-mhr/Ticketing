package com.example.services;

import com.example.dto.ShahkarResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ShahkarService {

    @Value("${shahkar.api.key}")
    private String apiKey;

    @Value("${shahkar.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public ShahkarService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ShahkarResponse verify(String mobile, String nationalCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", apiKey);

        Map<String, String> body = new HashMap<>();
        body.put("mobile", mobile);
        body.put("nationalCode", nationalCode);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<ShahkarResponse> response = restTemplate.postForEntity(
                    apiUrl, request, ShahkarResponse.class
            );

            return response.getBody();

        } catch (HttpClientErrorException e) {
            ShahkarResponse err = new ShahkarResponse();
            err.setData(false);
            err.setSuccess(false);
            err.setMessage("HTTP Error: " + e.getStatusCode());
            return err;
        }
    }

}

