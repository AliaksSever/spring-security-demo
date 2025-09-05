package com.itechart.springsecuritydemo.service.impl;

import com.itechart.profileserviceapi.dto.UpdateUserRequest;
import com.itechart.profileserviceapi.dto.UserDto;
import com.itechart.profileserviceapi.dto.RegisterRequest;
import com.itechart.profileserviceapi.enums.Role;
import com.itechart.springsecuritydemo.entity.User;
import com.itechart.springsecuritydemo.exception.UserNotFoundException;
import com.itechart.springsecuritydemo.mapper.UserReadMapper;
import com.itechart.springsecuritydemo.repository.UserRepository;
import com.itechart.springsecuritydemo.service.UserService;
import com.itechart.springsecuritydemo.service.UtilityUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UtilityUserService {

    private final UserRepository userRepository;

    private final KeycloakServiceImpl keycloakService;

    public Page<UserDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserReadMapper.INSTANCE::toDto);
    }

    public Optional<UserDto> getUserByUuid(UUID uuid) {
        return userRepository.findByUuid(uuid)
                .map(UserReadMapper.INSTANCE::toDto);
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

    public boolean checkRole(UUID uuid, String role) {
        return userRepository.findByUuid(uuid)
                .map(user -> user.getRoles().contains(Role.valueOf(role)))
                .orElse(false);
    }

    public List<UserDto> getExistingUsers(List<UUID> uuids) {
        return uuids.stream()
                .map(uuid -> getUserByUuid(uuid)
                        .orElseThrow(() -> new UserNotFoundException("User with uuid is not found".formatted(uuid))))
                .toList();
    }

    public List<UserDto> assignRole(List<UserDto> users, String role) {
        List<UserDto> newUsers = new ArrayList<>();
        for (UserDto userReadDto : users) {
            User user = UserReadMapper.INSTANCE.toEntity(userReadDto);
            List<Role> roles = user.getRoles();
            roles.add(Role.valueOf(role));
            user.setRoles(roles);
            userRepository.save(user);
            keycloakService.updateUserRole(user.getUuid(), role);
            userRepository.findByUuid(user.getUuid())
                    .map(UserReadMapper.INSTANCE::toDto)
                    .ifPresent(newUsers::add);
        }
        return newUsers;
    }
}
