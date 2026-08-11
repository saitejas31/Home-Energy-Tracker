package com.homeenergytracker.usage_service.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record DeviceDto(
    Long id,
    String name,
    @JsonAlias("deviceType") String type,
    String location,
    Long userId,
    Double energyConsumed
) {
}