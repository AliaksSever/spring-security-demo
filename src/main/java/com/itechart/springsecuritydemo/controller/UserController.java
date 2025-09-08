package com.itechart.springsecuritydemo.controller;

import com.itechart.profileserviceapi.dto.*;
import com.itechart.springsecuritydemo.exception.UserNotFoundException;
import com.itechart.springsecuritydemo.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserServiceImpl userService;

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
    public ResponseEntity<UserDto> findUserByUuid(@PathVariable UUID uuid) {
        return ResponseEntity.ok(userService.getUserByUuid(uuid).orElseThrow(() ->
                new UserNotFoundException(("User with uuid is not found".formatted(uuid)))));
    }

    @GetMapping("/hello")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_SUPERVISOR')")
    public ResponseEntity<String> helloPage(Principal principal) {
        log.info("Principal: {}", principal);
        log.info("Name: {}", principal.getName());
        return ResponseEntity.ok("Hello, " + principal.getName());
    }

    @PutMapping("update/{uuid}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_SUPERVISOR')")
    public ResponseEntity<UserDto> updateProfile(@PathVariable UUID uuid, @Valid @RequestBody UpdateUserRequest updateUserRequest) {
        UserDto userDto = userService.updateProfile(uuid, updateUserRequest);
        ;
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("delete/{uuid}")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPERVISOR', 'ROLE_USER')")
    public void deleteUser(@PathVariable UUID uuid) {
        userService.delete(uuid);
        log.info("User - {} - was successfully deleted", uuid);
    }

    @PostMapping("/checkRole")
    @PreAuthorize("hasAuthority('ROLE_SUPERVISOR')")
    public ResponseEntity<Boolean> checkUsersRole(@RequestBody CheckRoleRequest checkRoleRequest) {
        return ResponseEntity.ok(userService.checkRole(checkRoleRequest.uuid(), checkRoleRequest.role()));
    }

    @PutMapping("/assign")
    @PreAuthorize("hasAuthority('ROLE_SUPERVISOR')")
    public List<UserDto> assignRoles(@RequestBody AssignRoleRequest assignRoleRequest) {
        List<UserDto> users = userService.getExistingUsers(assignRoleRequest.uuids());
        return ResponseEntity.ok(userService.assignRole(users, String.valueOf(assignRoleRequest.role()))).getBody();
    }

    @PutMapping("/deleteRole")
    @PreAuthorize("hasAuthority('ROLE_SUPERVISOR')")
    public List<UserDto> deleteRole(@RequestBody DeleteRoleRequest deleteRoleRequest) {
        List<UserDto> users = userService.getExistingUsers(deleteRoleRequest.uuids());
        return ResponseEntity.ok(userService.deleteRole(users, String.valueOf(deleteRoleRequest.role()))).getBody();
    }

    @PostMapping("/findByRole")
    public ResponseEntity<Page<UserDto>> findByRoles(@RequestBody FindUsersByRolesRequest request) {
        return ResponseEntity.ok(userService.findUserByRoles(request.roles(), request.page(), request.size()));
    }


    @PostMapping("/bulk")
    public ResponseEntity<List<UserDto>> getUsersById(@RequestBody UserIdsRequest request){
        List<UserDto> existingUsers = userService.getExistingUsers(request.userIds());
        return ResponseEntity.ok(existingUsers);
    }

}
