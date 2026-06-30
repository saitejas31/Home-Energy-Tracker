package com.homeenergytracker.user_service.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String surname;
    private String address;
    private boolean alerting;
    public double energyAlertingThreshold;
}
