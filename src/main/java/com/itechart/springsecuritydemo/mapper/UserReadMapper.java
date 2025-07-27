package com.itechart.springsecuritydemo.mapper;

import com.itechart.springsecuritydemo.dto.UserReadDto;
import com.itechart.springsecuritydemo.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserReadMapper implements Mapper<User, UserReadDto> {

    @Override
    public UserReadDto map(User user) {
        return new UserReadDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }
}
