package com.itechart.springsecuritydemo.controller;

import com.itechart.springsecuritydemo.dto.UpdateUserRequest;
import com.itechart.springsecuritydemo.dto.UserDetailsDto;
import com.itechart.springsecuritydemo.dto.UserReadDto;
import com.itechart.springsecuritydemo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('SUPERVISOR')")
    public Page<UserReadDto> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return userService.findAll(pageable);
    }

    @GetMapping("/{uuid}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public UserReadDto findByUuid(@PathVariable UUID uuid) {
        return userService.getUserByUuid(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with uuid " + uuid + " not found"));
    }

    @GetMapping("/my_profile")
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal UserDetailsDto userDetailsDto){
        return ResponseEntity.ok(userService.getUserByUuid(userDetailsDto.getUuid()));
    }

    @PutMapping("/my_profile/update")
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<?> updateProfile(@AuthenticationPrincipal UserDetailsDto userDetailsDto, @Valid @RequestBody UpdateUserRequest updateUserRequest){
        if(!userService.checkPassword(updateUserRequest)){
            return ResponseEntity.badRequest().body(Map.of("message", "The passwords are not identity"));
        }
        return ResponseEntity.ok(userService.updateProfile(userDetailsDto.getUuid(), updateUserRequest));
    }
}
