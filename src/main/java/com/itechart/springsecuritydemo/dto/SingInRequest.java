package com.itechart.springsecuritydemo.dto;

import jakarta.validation.constraints.NotBlank;

public record SingInRequest(
        @NotBlank String username,
        @NotBlank String password) { }
