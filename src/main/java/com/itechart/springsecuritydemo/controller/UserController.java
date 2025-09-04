package com.itechart.springsecuritydemo.controller;

import  com.itechart.profileserviceapi.dto.UpdateUserRequest;
import  com.itechart.profileserviceapi.dto.UserDto;
import com.itechart.springsecuritydemo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/users")
public class UserController{

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_SUPERVISOR')")
    public Page<UserDto> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return userService.findAll(pageable);
    }

    @GetMapping("/{uuid}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<UserDto> getProfile(@PathVariable UUID uuid){
        return ResponseEntity.ok(userService.getUserByUuid(uuid).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with uuid " + uuid + " not found")));
    }

    @GetMapping("/hello")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_SUPERVISOR')")
    public ResponseEntity<String> helloPage(Principal principal){
        log.info("Principal: {}", principal);
        log.info("Name: {}", principal.getName());
        return ResponseEntity.ok("Hello, " + principal.getName());
    }

    @PutMapping("/{uuid}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_SUPERVISOR')")
    public ResponseEntity<UserDto> updateProfile(@PathVariable UUID uuid, @Valid @RequestBody UpdateUserRequest updateUserRequest){
        UserDto userDto = userService.updateProfile(uuid, updateUserRequest);;
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPERVISOR', 'ROLE_USER')")
    public void deleteUser(@PathVariable UUID uuid){
        userService.delete(uuid);
        log.info("User - {} - was successfully deleted", uuid);
    }
}
