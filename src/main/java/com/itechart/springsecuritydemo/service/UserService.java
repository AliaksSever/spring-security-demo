package com.itechart.springsecuritydemo.service;

import com.itechart.profileserviceapi.dto.UpdateUserRequest;
import com.itechart.profileserviceapi.dto.UserDto;
import com.itechart.profileserviceapi.dto.RegisterRequest;
import com.itechart.profileserviceapi.enums.Role;
import com.itechart.springsecuritydemo.entity.User;
import com.itechart.springsecuritydemo.mapper.UserReadMapper;
import com.itechart.springsecuritydemo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final KeycloakService keycloakService;

    public Page<UserDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserReadMapper.INSTANCE::toDto);
    }

    public Optional<UserDto> getUserByUuid(UUID uuid) {
        return userRepository.findByUuid(uuid).map(UserReadMapper.INSTANCE::toDto);
    }

    public void register(RegisterRequest request) {
        userRepository.save(UserReadMapper.INSTANCE.toEntity(
                UserDto.builder()
                        .username(request.username())
                        .uuid(UUID.randomUUID())
                        .email(request.email())
                        .roles(Collections.singletonList(Role.ROLE_USER))
                        .build()
        ));
    }

    @Transactional
    public void delete(UUID uuid) {
        userRepository.deleteByUuid(uuid);
        keycloakService.deleteKeycloakUser(uuid);
    }

    public boolean isExist(RegisterRequest request) {
        return userRepository.existsByEmail(request.email());
    }

    @Transactional
    public UserDto updateProfile(UUID uuid, UpdateUserRequest updateUserRequest) {
        User user = userRepository.findByUuid(uuid).orElseThrow();
        user.setEmail(updateUserRequest.email());
        user.setCity(updateUserRequest.city());
        user.setPhoneNumber(updateUserRequest.phoneNumber());
        keycloakService.updateKeycloakUser(uuid, updateUserRequest);
        return UserReadMapper.INSTANCE.toDto(userRepository.save(user));
    }
    public boolean checkRole(UUID uuid, String role){
        return userRepository.findByUuid(uuid)
                .map(user -> user.getRoles().contains(Role.valueOf(role)))
                .orElse(false);

    }
}
