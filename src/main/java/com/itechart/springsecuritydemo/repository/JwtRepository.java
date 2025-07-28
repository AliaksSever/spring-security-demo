package com.itechart.springsecuritydemo.repository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtRepository {
    String generateToken(Authentication authentication);
    String extractUserName(String token);
    boolean isTokenValid(String token, UserDetails userDetails);
}
