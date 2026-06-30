package com.homeenergytracker.device_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.homeenergytracker.device_service.entity.Device;

/**
 * DeviceRepository
 */
public interface DeviceRepository extends JpaRepository<Device,Long> {

}
