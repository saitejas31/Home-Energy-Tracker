package com.homeenergytracker.user_service.entity;

import jakarta.persistence.*;
import lombok.*;


@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String surname;
    private String address;
    private boolean alerting;
    public double energyAlertingThreshold;
}
