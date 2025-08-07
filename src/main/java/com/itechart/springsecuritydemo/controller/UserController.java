package com.itechart.springsecuritydemo.controller;

import com.itechart.springsecuritydemo.dto.UpdateUserRequest;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
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

    @GetMapping("/my_profile/{uuid}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<UserReadDto> getProfile(@PathVariable UUID uuid){
        return ResponseEntity.ok(userService.getUserByUuid(uuid).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with uuid " + uuid + " not found")));
    }
    @GetMapping("/hello")
    @PreAuthorize("hasAuthority('USER')")
    public String helloPage(Principal principal){
        System.out.println("Principal: " + principal);
        System.out.println("Name: " + principal.getName());
        return "Hello АААААААА, " + principal.getName();
    }
    @PutMapping("/my_profile/{uuid}/update")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> updateProfile(@PathVariable UUID uuid, @Valid @RequestBody UpdateUserRequest updateUserRequest){
        if(userService.checkPassword(updateUserRequest)){
            return ResponseEntity.badRequest().body(Map.of("message", "Password dont match"));
        }
        UserReadDto userReadDto = userService.updateProfile(uuid, updateUserRequest);;
        return ResponseEntity.ok(userReadDto);
    }

    @DeleteMapping("/delete/{uuid}")
    @PreAuthorize("hasAnyAuthority('SUPERVISOR', 'USER')")
    public ResponseEntity<String> deleteUser(@PathVariable UUID uuid){
        userService.delete(uuid);
        return ResponseEntity.ok("User was successfully delete");
    }
}
