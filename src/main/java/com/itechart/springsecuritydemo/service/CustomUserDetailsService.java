package com.itechart.springsecuritydemo.service;

import com.itechart.springsecuritydemo.dto.UserDetailsDto;
import com.itechart.springsecuritydemo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

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
}
