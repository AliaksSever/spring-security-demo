package com.itechart.springsecuritydemo.service;

import com.itechart.profileserviceapi.dto.RegisterRequest;
import com.itechart.profileserviceapi.dto.UserDto;
import com.itechart.profileserviceapi.enums.Role;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface UtilityUserService {
    boolean isExist(RegisterRequest request);
    boolean checkRole(UUID uuid, String role);
    List<UserDto> getExistingUsers(List<UUID> uuids);

    Page<UserDto> findUserByRoles(Set<Role> roles, int page, int size);

}
