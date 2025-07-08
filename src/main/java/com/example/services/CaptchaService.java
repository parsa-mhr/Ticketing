package com.example.services;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class CaptchaService {

    @Value("${cloudflare.turnstile.secret}")
    private String secretKey;

    private static final String VERIFY_URL = "https://challenges.cloudflare.com/turnstile/v0/siteverify";

    public boolean validateToken(String token) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, String> request = new HashMap<>();
        request.put("secret", secretKey);
        request.put("response", token);

        try {
            TurnstileResponse response = restTemplate.postForObject(
                    VERIFY_URL,
                    request,
                    TurnstileResponse.class
            );

            return response != null && response.isSuccess();
        } catch (Exception e) {
            return false;
        }
    }

    public static class TurnstileResponse {
        private boolean success;
        private String[] errorCodes;
        private String challengeTs;
        private String hostname;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String[] getErrorCodes() {
            return errorCodes;
        }

        public void setErrorCodes(String[] errorCodes) {
            this.errorCodes = errorCodes;
        }

        public String getChallengeTs() {
            return challengeTs;
        }

        public void setChallengeTs(String challengeTs) {
            this.challengeTs = challengeTs;
        }

        public String getHostname() {
            return hostname;
        }

        public void setHostname(String hostname) {
            this.hostname = hostname;
        }
    }
}
