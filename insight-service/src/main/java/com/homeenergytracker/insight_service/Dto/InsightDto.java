package com.homeenergytracker.insight_service.Dto;

import lombok.Builder;

@Builder
public record InsightDto(
    Long userId,
    String tips,
    double energyUsed
) {
    
}
