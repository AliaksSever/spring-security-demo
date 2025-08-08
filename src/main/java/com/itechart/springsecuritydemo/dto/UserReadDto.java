package com.itechart.springsecuritydemo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itechart.springsecuritydemo.entity.Role;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Builder(toBuilder = true)
@Value
public class UserReadDto {
    @JsonIgnore
    Long id;
    @JsonIgnore
    UUID uuid;
    String username;
    String phoneNumber;
    String city;
    String email;
    Role role;

}
