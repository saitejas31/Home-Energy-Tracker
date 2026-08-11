package com.homeenergytracker.insight_service.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;

import com.homeenergytracker.insight_service.Dto.InsightDto;
import com.homeenergytracker.insight_service.service.InsightService;

@RestController
@RequestMapping("/api/v1/insight")
public class InsightController {

    private final InsightService insightService;

    public InsightController(InsightService insightService){
        this.insightService=insightService;
    }

    @GetMapping("/saving-tips/{userId}")
    public ResponseEntity<InsightDto> getSavingTips(@PathVariable Long userId){
        final InsightDto dto = insightService.getSavingTips(userId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/overview/{userId}")
    public ResponseEntity<InsightDto> getOverview(@PathVariable Long userId){
        final InsightDto dto = insightService.getOverview(userId);
        return ResponseEntity.ok(dto);
    }
    
}
