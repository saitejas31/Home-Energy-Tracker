package com.homeenergytracker.user_service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.homeenergytracker.user_service.entity.User;
import com.homeenergytracker.user_service.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class UserServiceApplicationTests {

	@Autowired
	private UserRepository userRepository;
	private static final int NUMBER_OF_USERS = 10;

	@Test
	void contextLoads() {
	}

	@Test
	void addUsers(){
		for(int i=1;i<=NUMBER_OF_USERS;i++){
			User user = User.builder()
					.name("User"+i)
					.email("User"+i+"@gmail.com")
					.surname("surname"+i)
					.address("address"+i)
					.alerting(i%2==0)
					.energyAlertingThreshold(1000.0+i)
					.build();
			userRepository.save(user);
		}
		log.info("User Repo has {} users", userRepository.count());
	}

}
