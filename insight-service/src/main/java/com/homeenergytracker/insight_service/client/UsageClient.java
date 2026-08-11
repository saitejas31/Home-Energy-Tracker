package com.homeenergytracker.insight_service.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

import com.homeenergytracker.insight_service.Dto.UsageDto;

@Component
public class UsageClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;


    public UsageClient(RestTemplate restTemplate,@Value("${usage.service.url}") String baseUrl){
        this.restTemplate=restTemplate;
        this.baseUrl=baseUrl;
    }

    public UsageDto getXDaysUsageForUser(Long userId,int days){
        String url = UriComponentsBuilder
                    .fromUriString(baseUrl)
                    .path("/{userId}")
                    .queryParam("days",days)
                    .buildAndExpand(userId)
                    .toUriString();

        final ResponseEntity<UsageDto> response = restTemplate.getForEntity(url,UsageDto.class);

        return response.getBody();
    }

    


}

