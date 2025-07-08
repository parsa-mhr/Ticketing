package com.example.controllers;

import com.example.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/status")
public class StatusController {

    private final JwtUtil jwtUtil;
    private final Set<String> onlineStatus = ConcurrentHashMap.newKeySet();

    public StatusController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> setStatus(@RequestHeader("Authorization") String authHeader) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.put("error", "Invalid Authorization header");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            String token = authHeader.substring(7);  // Remove "Bearer "
            if (!jwtUtil.validateToken(token)) {
                response.put("error", "Invalid or expired token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            String email = jwtUtil.extractUsername(token);
            onlineStatus.add(email);

            response.put("message", email + " is now online");
            response.put("onlineUsersCount", onlineStatus.size());
            response.put("onlineUsers", onlineStatus);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> response = new HashMap<>();
        response.put("onlineUsersCount", onlineStatus.size());
        response.put("onlineUsers", onlineStatus);
        onlineStatus.clear();
        return ResponseEntity.ok(response);
    }
}
