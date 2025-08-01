package com.itechart.springsecuritydemo.service;


import com.itechart.springsecuritydemo.dto.UpdateUserRequest;
import com.itechart.springsecuritydemo.dto.UserDetailsDto;
import com.itechart.springsecuritydemo.dto.UserReadDto;
import com.itechart.springsecuritydemo.dto.RegisterRequest;
import com.itechart.springsecuritydemo.entity.Role;
import com.itechart.springsecuritydemo.entity.User;
import com.itechart.springsecuritydemo.mapper.UserReadMapper;
import com.itechart.springsecuritydemo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Page<UserReadDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserReadMapper.INSTANCE::toDto);
    }

    public Optional<UserReadDto> getUserByUuid(UUID uuid) {
        return userRepository.findByUuid(uuid).map(UserReadMapper.INSTANCE::toDto);
    }

    public void register(RegisterRequest request) {
        userRepository.save(UserReadMapper.INSTANCE.toEntity(
                UserReadDto.builder()
                        .username(request.username())
                        .uuid(UUID.randomUUID())
                        .email(request.email())
                        .role(Role.USER)
                        .password(passwordEncoder.encode(request.password()))
                        .build()
        ));
    }

    public boolean isExist(RegisterRequest request) {
        return userRepository.existsByEmail(request.email());
    }

    public UserReadDto updateProfile(UUID uuid, UpdateUserRequest updateUserRequest) {
        User user = userRepository.findByUuid(uuid).orElseThrow();
        user.setEmail(updateUserRequest.email());
        user.setPassword(passwordEncoder.encode(updateUserRequest.newPassword()));
        user.setUsername(updateUserRequest.username());
        return new UserDetailsDto(
                UserReadMapper.INSTANCE.toDto(userRepository.save(user))).getUser();
    }

    public boolean checkPassword(UpdateUserRequest updateUserRequest) {
        return updateUserRequest.newPassword().equals(updateUserRequest.confPassword());
    }
}
