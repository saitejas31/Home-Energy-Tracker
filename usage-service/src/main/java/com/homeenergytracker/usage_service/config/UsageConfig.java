package com.homeenergytracker.usage_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class UsageConfig {

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
