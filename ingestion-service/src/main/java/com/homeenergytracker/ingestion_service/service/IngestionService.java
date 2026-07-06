package com.homeenergytracker.ingestion_service.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.homeenergytracker.ingestion_service.dto.EnergyUsageDto;
import com.homeenergytracker.ingestion_service.kafka.event.EnergyUsageEvent;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class IngestionService {
    private final KafkaTemplate<String,EnergyUsageEvent> kafkaTemplate;

    public IngestionService(KafkaTemplate<String,EnergyUsageEvent> kafkaTemplate){
        this.kafkaTemplate=kafkaTemplate;
    }

    public void ingestEnergyUsage(EnergyUsageDto energyUsageDto){
        EnergyUsageEvent energyUsageEvent = EnergyUsageEvent.builder()
                                            .deviceId(energyUsageDto.deviceId())
                                            .energyConsumed(energyUsageDto.energyConsumed())
                                            .timestamp(energyUsageDto.timestamp())
                                            .build();

        kafkaTemplate.send("energy-usage",energyUsageEvent);
        log.info("Ingested energy usage event :{}",energyUsageEvent);
    }


}
