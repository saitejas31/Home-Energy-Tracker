package com.homeenergytracker.device_service.entity;

import com.homeenergytracker.device_service.model.DeviceType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@Table(name = "device")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Device {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private DeviceType deviceType;
    private String location;
    private Long userId;
}
