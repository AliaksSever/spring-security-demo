package com.itechart.springsecuritydemo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterRequest(@NotBlank
                              @Pattern(regexp = "[A-Za-z]+\\s[A-Za-z]+",
                                      message = "Please enter both first and last name")
                              String username,
                              @NotBlank String password,
                              @NotBlank @Email @Pattern(regexp = "[a-zA-Z_0-9]+@gmail\\.com",
                                      message = "Please enter a valid gmail address")
                              String email) {
}
