package com.itechart.springsecuritydemo.controller;

import  com.itechart.profileserviceapi.dto.UpdateUserRequest;
import  com.itechart.profileserviceapi.dto.UserDto;
import  com.itechart.profileserviceapi.api.UserClient;
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
@RequestMapping("/users")
public class UserController implements UserClient{

    private final UserService userService;

    @Override
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_SUPERVISOR')")
    public Page<UserDto> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return userService.findAll(pageable);
    }

    @Override
    @GetMapping("/my_profile/{uuid}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<UserDto> getProfile(@PathVariable UUID uuid){
        return ResponseEntity.ok(userService.getUserByUuid(uuid).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with uuid " + uuid + " not found")));
    }

    @Override
    @GetMapping("/hello")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<String> helloPage(Principal principal){
        log.info("Principal: {}", principal);
        log.info("Name: {}", principal.getName());
        return ResponseEntity.ok("Hello, " + principal.getName());
    }

    @Override
    @PutMapping("/my_profile/{uuid}/update")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_SUPERVISOR')")
    public ResponseEntity<?> updateProfile(@PathVariable UUID uuid, @Valid @RequestBody UpdateUserRequest updateUserRequest){
        UserDto userReadDto = userService.updateProfile(uuid, updateUserRequest);;
        return ResponseEntity.ok(userReadDto);
    }

    @Override
    @DeleteMapping("/delete/{uuid}")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPERVISOR', 'ROLE_USER')")
    public ResponseEntity<String> deleteUser(@PathVariable UUID uuid){
        userService.delete(uuid);
        return ResponseEntity.ok("User was successfully delete");
    }
}
