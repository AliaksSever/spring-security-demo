package com.itechart.springsecuritydemo.controller;

import com.itechart.springsecuritydemo.dto.RegisterRequest;
import com.itechart.springsecuritydemo.dto.SingInRequest;
import com.itechart.springsecuritydemo.service.JwtService;
import com.itechart.springsecuritydemo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        if (userService.isExist(request)) {
            return ResponseEntity.badRequest().body("User with email " + request.email() + " already exists");
        }
        userService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/sign-in")
    public ResponseEntity<String> signIn(@RequestBody @Valid SingInRequest singInRequest) {
        String token;
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(singInRequest.email(), singInRequest.password()));
             token = jwtService.generateToken(authentication);
        } catch (AuthenticationException exception){
            return ResponseEntity.badRequest().body("User doesn't exist");
        }
        return ResponseEntity.ok(token);
    }
}
