package com.homeenergytracker.device_service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.homeenergytracker.device_service.entity.Device;
import com.homeenergytracker.device_service.model.DeviceType;
import com.homeenergytracker.device_service.repository.DeviceRepository;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class DeviceServiceApplicationTests {

	private static final int NUMBER_OF_DEVICES = 200;
	private static final int NUMBER_OF_USERS = 10;
	@Autowired
	private DeviceRepository deviceRepository;

	@Test
	void contextLoads() {
	}


	@Disabled
	@Test
	void createDevices(){
		for(int i=1;i<=NUMBER_OF_DEVICES;i++){
			var device = Device.builder()
					.name("Device" + i)
					.deviceType(DeviceType.values()[i%DeviceType.values().length])
					.location("Location" + (i%4+1))
					.userId((long)(i%NUMBER_OF_USERS)+1)
					.build();
			deviceRepository.save(device);
		}
		log.info("Device Repo has {} devices", deviceRepository.count());
	}

}
