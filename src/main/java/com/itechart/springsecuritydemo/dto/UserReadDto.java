package com.itechart.springsecuritydemo.dto;

import com.itechart.springsecuritydemo.entity.Role;
import lombok.Value;

@Value
public class UserReadDto{

    Long id;
    String username;
    String email;
    Role role;
}
