package com.homeenergytracker.user_service.service;

import com.homeenergytracker.user_service.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.homeenergytracker.user_service.entity.User;
import com.homeenergytracker.user_service.repository.UserRepository;


@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto createUser(UserDto userDto) {
        log.info("Creating user: {}", userDto);

        final User user = User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .surname(userDto.getSurname())
                .address(userDto.getAddress())
                .alerting(userDto.isAlerting())
                .energyAlertingThreshold(userDto.getEnergyAlertingThreshold())
                .build();
        userRepository.save(user);
        log.info("User created successfully: {}", userDto);
        return toDto(user);
    }

    public UserDto getUserById(Long id) {
        log.info("Fetching user with id: {}", id);
        final User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            log.warn("User with id {} not found", id);
            return null;
        }
        log.info("User fetched successfully: {}", user);
        return toDto(user);
    }

    public void updateUser(Long id,UserDto userDto) {
        log.info("Updating user with id: {}", id);
        final User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser == null) {
            log.warn("User with id {} not found", id);
            throw new RuntimeException("User not found");
        }
        existingUser.setName(userDto.getName());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setSurname(userDto.getSurname());
        existingUser.setAddress(userDto.getAddress());
        existingUser.setAlerting(userDto.isAlerting());
        existingUser.setEnergyAlertingThreshold(userDto.getEnergyAlertingThreshold());
        userRepository.save(existingUser);
        log.info("User updated successfully: {}", userDto);
    }

    public void deleteUser(Long id) {
        log.info("Deleting user with id: {}", id);
        final User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser == null) {
            log.warn("User with id {} not found", id);
            throw new RuntimeException("User not found");
        }
        userRepository.delete(existingUser);
        log.info("User deleted successfully with id: {}", id);
    }

    public UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .surname(user.getSurname())
                .address(user.getAddress())
                .alerting(user.isAlerting())
                .energyAlertingThreshold(user.getEnergyAlertingThreshold())
                .build();
    }
}
