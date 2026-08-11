package com.homeenergytracker.insight_service.Dto;

import lombok.Builder;

@Builder
public record DeviceDto(Long id ,String name,String type, String location,double energyConsumed) {
}
