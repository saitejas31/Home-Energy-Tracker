package com.homeenergytracker.ingestion_service.controller;

import org.springframework.web.bind.annotation.*;

import com.homeenergytracker.ingestion_service.dto.EnergyUsageDto;
import com.homeenergytracker.ingestion_service.service.IngestionService;

@RestController
@RequestMapping("/api/v1/ingestion")
public class IngestionController {

    private final IngestionService ingestionService;

    public IngestionController(IngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @PostMapping
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public void ingestData(@RequestBody EnergyUsageDto energyUsageDto) {
        System.out.println("hello inside controler");
        ingestionService.ingestEnergyUsage(energyUsageDto);
    }
}
