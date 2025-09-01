package com.itechart.springsecuritydemo.dto;

import com.itechart.profileserviceapi.enums.Role;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record UserUpdateEvent(UUID uuid,
                              List<Role> newRoles,
                              LocalDateTime updatedAt) {
}
