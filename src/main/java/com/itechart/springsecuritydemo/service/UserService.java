package com.itechart.springsecuritydemo.service;

import com.itechart.profileserviceapi.dto.RegisterRequest;
import com.itechart.profileserviceapi.dto.UpdateUserRequest;
import com.itechart.profileserviceapi.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    Page<UserDto> findAll(Pageable pageable);
    Optional<UserDto> getUserByUuid(UUID uuid);
    void register(RegisterRequest request);
    void delete(UUID uuid);
    UserDto updateProfile(UUID uuid, UpdateUserRequest updateUserRequest);
    List<UserDto> assignRole(List<UserDto> users, String role);
}
