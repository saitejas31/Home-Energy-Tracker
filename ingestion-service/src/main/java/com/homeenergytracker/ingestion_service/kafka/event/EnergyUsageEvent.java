package com.homeenergytracker.ingestion_service.kafka.event;

import java.time.Instant;

import lombok.Builder;

@Builder
public record EnergyUsageEvent(
    Long deviceId,
    Double energyConsumed,
    Instant timestamp
) {
    
}
