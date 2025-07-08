package com.example.services;

import com.example.dto.OtpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
@Service
public class OtpService {


        @Value("${otp.api.key}")
        private String apiKey;

        @Value("${otp.api.url}")
        private String apiUrl;

        private final RestTemplate restTemplate;

        public OtpService(RestTemplate restTemplate) {
            this.restTemplate = restTemplate;
        }

        public OtpResponse sendOtp(String mobile, String code) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", apiKey);

            Map<String, String> body = new HashMap<>();
            body.put("mobile", mobile);
            body.put("code", code);

            HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

            try {
                ResponseEntity<OtpResponse> response = restTemplate.postForEntity(
                        apiUrl, request, OtpResponse.class
                );

                return response.getBody();

            } catch (HttpClientErrorException e) {
                OtpResponse err = new OtpResponse();
                err.setSuccess(false);
                err.setMessage("HTTP Error: " + e.getStatusCode());
                return err;
            }
        }

    }

