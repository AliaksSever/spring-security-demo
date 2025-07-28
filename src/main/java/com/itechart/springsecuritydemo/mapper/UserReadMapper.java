package com.itechart.springsecuritydemo.mapper;

import com.itechart.springsecuritydemo.dto.UserReadDto;
import com.itechart.springsecuritydemo.entity.User;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
@Mapper
public interface UserReadMapper {
    UserReadMapper INSTANCE = Mappers.getMapper(UserReadMapper.class);

    @Mapping(target = "id")
    UserReadDto toDto(User user);

    @Mapping(target = "id")
    User toEntity(UserReadDto userDTO);
}
