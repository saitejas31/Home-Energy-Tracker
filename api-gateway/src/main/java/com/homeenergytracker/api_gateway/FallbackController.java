package com.homeenergytracker.api_gateway;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1/fallback")
public class FallbackController {
    
    @GetMapping("/user")
    public ResponseEntity<Map<String, Object>> userFallback() {
        return createFallbackResponse("user-service");
    }

    @GetMapping("/device")
    public ResponseEntity<Map<String, Object>> deviceFallback() {
        return createFallbackResponse("device-service");
    }

    @GetMapping("/ingestion")
    public ResponseEntity<Map<String, Object>> ingestionFallback() {
        return createFallbackResponse("ingestion-service");
    }
    
    @GetMapping("/insight")
    public ResponseEntity<Map<String, Object>> insightFallback() {
        return createFallbackResponse("insight-service");
    }

    @GetMapping("/usage")
    public ResponseEntity<Map<String, Object>> usageFallback() {
        return createFallbackResponse("usage-service");
    }
    public ResponseEntity<Map<String, Object>> createFallbackResponse(String serviceName) {
        Map<String, Object> response =new HashMap<>();
        response.put("timestamp",LocalDateTime.now());
        response.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        response.put("message",serviceName + "is currently unavailable");

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);

    }


}
