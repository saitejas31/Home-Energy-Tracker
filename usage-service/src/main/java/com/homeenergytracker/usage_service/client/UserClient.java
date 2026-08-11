package com.homeenergytracker.usage_service.client;

import com.homeenergytracker.usage_service.dto.UserDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class UserClient {

    private final RestTemplate restTemplate;

    private final String baseUrl;

    public UserClient(@Value("${user.service.url}") String baseUrl, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    @Retry(name = "userService", fallbackMethod = "getUserByIdFallback")
    @CircuitBreaker(name = "userService", fallbackMethod = "getUserByIdFallback")
    public UserDto getUserById(Long userId) {
        String url = UriComponentsBuilder
                .fromUriString(baseUrl)
                .path("/{userId}")
                .buildAndExpand(userId)
                .toUriString();

        ResponseEntity<UserDto> response = restTemplate.getForEntity(url, UserDto.class);
        return response.getBody();
    }

    public UserDto getUserByIdFallback(Long userId,Throwable throwable){
        return new UserDto(userId, "", "", "", "", false, 0);
    }
}