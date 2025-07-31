package com.itechart.springsecuritydemo.controller;

import com.itechart.springsecuritydemo.dto.UserReadDto;
import com.itechart.springsecuritydemo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
}
