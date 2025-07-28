package com.itechart.springsecuritydemo.service;

import com.itechart.springsecuritydemo.dto.UserDetailsDto;
import com.itechart.springsecuritydemo.dto.UserReadDto;
import com.itechart.springsecuritydemo.mapper.UserReadMapper;
import com.itechart.springsecuritydemo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserReadMapper userReadMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> UserDetailsDto.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .authorities(Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())))
                        .build()
                )
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public Page<UserReadDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(userReadMapper::map);
    }

    public Optional<UserReadDto> findById(Long id) {
        return userRepository.findById(id).map(userReadMapper::map);
    }
}
