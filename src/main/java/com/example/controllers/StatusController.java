package com.example.controllers;

import com.example.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/status")
public class StatusController {

    private final JwtUtil jwtUtil;
    private final Map<String, Long> userLastActiveMap = new ConcurrentHashMap<>();
    private static final long EXPIRATION_TIME = 300_000; // 1.4 minutes

    public StatusController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Scheduled(fixedRate = 60_000)
    public void cleanupInactiveUsers() {
        long currentTime = System.currentTimeMillis();
        userLastActiveMap.entrySet().removeIf(entry ->
                (currentTime - entry.getValue()) > EXPIRATION_TIME
        );
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> setStatus(@RequestHeader("Authorization") String authHeader) {
        String username = validateToken(authHeader);
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid token"));
        }

        userLastActiveMap.put(username, System.currentTimeMillis());
        return ResponseEntity.ok(Map.of(
                "message", username + " is online",
                "onlineUsersCount", userLastActiveMap.size()
        ));
    }

    @PostMapping("/heartbeat")
    public ResponseEntity<Void> heartbeat(@RequestHeader("Authorization") String authHeader) {
        String username = validateToken(authHeader);
        if (username != null) {
            userLastActiveMap.put(username, System.currentTimeMillis());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getStatus() {
        long currentTime = System.currentTimeMillis();
        Set<String> activeUsers = userLastActiveMap.entrySet().stream()
                .filter(entry -> (currentTime - entry.getValue()) <= EXPIRATION_TIME)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        return ResponseEntity.ok(Map.of(
                "onlineUsersCount", activeUsers.size(),
                "onlineUsers", activeUsers
        ));
    }

    private String validateToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        String token = authHeader.substring(7);
        return jwtUtil.validateToken(token) ? jwtUtil.extractUsername(token) : null;
    }
}