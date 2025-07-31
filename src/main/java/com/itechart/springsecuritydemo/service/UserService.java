package com.itechart.springsecuritydemo.service;

import com.itechart.springsecuritydemo.dto.UserDetailsDto;
import com.itechart.springsecuritydemo.dto.UserReadDto;
import com.itechart.springsecuritydemo.dto.RegisterRequest;
import com.itechart.springsecuritydemo.entity.Role;
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

    public void register(RegisterRequest request){
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
}
