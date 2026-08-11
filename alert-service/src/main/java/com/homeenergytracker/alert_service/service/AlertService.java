package com.homeenergytracker.alert_service.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.homeenergytracker.alert_service.kafka.event.AlertingEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AlertService {

    private final EmailService emailService;

    @KafkaListener(topics = "energy-alerts",groupId = "alert-service")
    public void energyUsageAlertEvent(AlertingEvent alertingEvent){
        log.info("Received alert event : {}",alertingEvent);

        // Send email alert
        final String subject = "High Energy Usage Alert for User" + alertingEvent.getUserId();
        final String message = "Alert: " + alertingEvent.getMessage() +
                    "\n Threshold : " + alertingEvent.getThreshold() +
                    "\n Energy Consumed : " + alertingEvent.getEnergyConsumed();
        emailService.sendEmail(alertingEvent.getEmail(), subject, message, alertingEvent.getUserId());
        
    }
    

    
}
