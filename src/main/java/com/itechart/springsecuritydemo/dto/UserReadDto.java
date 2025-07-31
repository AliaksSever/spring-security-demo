package com.itechart.springsecuritydemo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itechart.springsecuritydemo.entity.Role;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class UserReadDto{

    Long id;
    UUID uuid;
    String username;
    @JsonIgnore
    String password;
    String email;
    Role role;

}
