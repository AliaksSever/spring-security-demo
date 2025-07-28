package com.itechart.springsecuritydemo.controller;

import com.itechart.springsecuritydemo.dto.RegisterRequest;
import com.itechart.springsecuritydemo.dto.SingInRequest;
import com.itechart.springsecuritydemo.service.JwtService;
import com.itechart.springsecuritydemo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        userService.register(request);
        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<String> signIn(@RequestBody SingInRequest singInRequest){
        Authentication authentication =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(singInRequest.username(), singInRequest.password()));
        jwtService.generateToken(authentication);
        return ResponseEntity.ok(authentication.getName());
    }
}
