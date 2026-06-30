package com.homeenergytracker.device_service.service;

import org.springframework.stereotype.Service;

import com.homeenergytracker.device_service.dto.DeviceDto;
import com.homeenergytracker.device_service.entity.Device;
import com.homeenergytracker.device_service.exception.DeviceNotFoundException;
import com.homeenergytracker.device_service.model.DeviceType;
import com.homeenergytracker.device_service.repository.DeviceRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DeviceService {

    private final DeviceRepository deviceRepository;

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public DeviceDto getDeviceById(Long id){
        log.info("Getting device by id");
        Device device = deviceRepository.findById(id).orElseThrow(()-> new DeviceNotFoundException("Device not found"));
        log.info("Device found");
        return mapToDto(device);
    }

    public DeviceDto createDevice(DeviceDto deviceDto){
        log.info("Creating device");
        Device device = mapToEntity(deviceDto);
        Device savedDevice = deviceRepository.save(device);
        log.info("Device created");
        return mapToDto(savedDevice);
    }

    public DeviceDto updateDevice(Long id,DeviceDto deviceDto){
        log.info("Updating device");
        Device device = deviceRepository.findById(id).orElseThrow(()-> new DeviceNotFoundException("Device not found"));
        device.setName(deviceDto.getName());
        device.setDeviceType(DeviceType.valueOf(deviceDto.getDeviceType()));
        device.setLocation(deviceDto.getLocation());
        device.setUserId(deviceDto.getUserId());
        Device updatedDevice = deviceRepository.save(device);
        log.info("Device updated");
        return mapToDto(updatedDevice);
    }

    public void deleteDevice(Long id){
        log.info("Deleting device");
        Device device = deviceRepository.findById(id).orElseThrow(()-> new DeviceNotFoundException("Device not found"));
        log.info("Device found");
        deviceRepository.delete(device);
    }

    public Device mapToEntity(DeviceDto deviceDto){
        return Device.builder()
        .id(deviceDto.getId())
        .name(deviceDto.getName())
        .deviceType(DeviceType.valueOf(deviceDto.getDeviceType()))
        .location(deviceDto.getLocation())
        .userId(deviceDto.getUserId())
        .build();
    }

    public DeviceDto mapToDto(Device device){
        return DeviceDto.builder()
        .id(device.getId())
        .name(device.getName())
        .deviceType(device.getDeviceType().name())
        .location(device.getLocation())
        .userId(device.getUserId())
        .build();
    }

}
