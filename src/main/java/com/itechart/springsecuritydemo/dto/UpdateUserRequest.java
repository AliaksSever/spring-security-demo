package com.itechart.springsecuritydemo.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(@NotBlank String username, @NotBlank String confPassword) {
}
