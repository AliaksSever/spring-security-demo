package com.itechart.springsecuritydemo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


public record UpdateUserRequest(@NotBlank @Email @Pattern(regexp = "[a-zA-Z_0-9]+@gmail\\.com",
        message = "Please enter a valid gmail address") String email, @Pattern(regexp = "\\+375\\d{9}") String phoneNumber, String city) {
}
