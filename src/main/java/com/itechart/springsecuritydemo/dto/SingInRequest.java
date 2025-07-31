package com.itechart.springsecuritydemo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SingInRequest(
        @NotBlank
        @Pattern(regexp = "[A-Za-z]+\\s[A-Za-z]+",
                 message = "Please enter both first and last name")
        String username,
        @NotBlank String password) { }
