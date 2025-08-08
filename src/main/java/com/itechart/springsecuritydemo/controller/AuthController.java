package com.itechart.springsecuritydemo.controller;

import com.itechart.springsecuritydemo.dto.RegisterRequest;
import com.itechart.springsecuritydemo.dto.SingInRequest;
import com.itechart.springsecuritydemo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        if (userService.isExist(request)) {
            return ResponseEntity.badRequest().body("User with email " + request.email() + " already exists");
        }
        userService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }

}
