package com.homeenergytracker.usage_service.service;

import com.influxdb.annotations.Column;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.homeenergytracker.usage_service.kafka.event.EnergyUsageEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UsageService {

    private InfluxDBClient influxDBClient;

    public UsageService(InfluxDBClient influxDBClient){
        this.influxDBClient=influxDBClient;
    }
    
    @Value("${influx.bucket}")
    private String influxBucket;
    
    @Value("${influx.org}")
    private String influxOrg;

    @KafkaListener(topics = "energy-usage",groupId = "usage-service")
    public void energyUsageEvent(EnergyUsageEvent energyUsageEvent){
        log.info("Received energy-usage event: {}", energyUsageEvent);
                Point point = Point.measurement("energy_usage")
                .addTag("deviceId", String.valueOf(energyUsageEvent.deviceId()))
                .addField("energyConsumed", energyUsageEvent.energyConsumed())
                .time(energyUsageEvent.timestamp(), WritePrecision.MS);
        influxDBClient.getWriteApiBlocking().writePoint(influxBucket, influxOrg, point);
    }

    @Scheduled(cron = "")
}
