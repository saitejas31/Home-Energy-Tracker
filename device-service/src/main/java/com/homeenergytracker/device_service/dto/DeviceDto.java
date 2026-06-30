package com.homeenergytracker.device_service.dto;

import lombok.*;

/**
 * DeviceDto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeviceDto {

    private Long id;
    private String name;
    private String deviceType;
    private String location;
    private Long userId;



}
