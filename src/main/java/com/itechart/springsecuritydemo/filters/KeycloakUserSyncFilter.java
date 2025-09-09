package com.itechart.springsecuritydemo.filters;

import com.itechart.profileserviceapi.enums.Role;
import com.itechart.springsecuritydemo.entity.User;
import com.itechart.springsecuritydemo.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.oauth2.jwt.Jwt;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeycloakUserSyncFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            log.info("KeycloakUserSyncFilter been triggered");

            Jwt jwt = jwtAuth.getToken();
            String keycloakId = jwt.getSubject();
            String username = jwt.getClaim("preferred_username");
            String email = jwt.getClaim("email");
            List<String> roles = jwt.getClaimAsStringList("roles");

            Set<Role> currentRoles = roles.stream()
                    .filter(role -> role != null && role.startsWith("ROLE_"))
                    .map(Role::valueOf)
                    .collect(Collectors.toSet());
            UUID uuid = UUID.fromString(keycloakId);

            userRepository.findByUuid(uuid).ifPresentOrElse(
                    existingUser -> {
                        boolean updated = false;

                        if (!existingUser.getRoles().equals(currentRoles)) {
                            existingUser.setRoles(currentRoles);
                            updated = true;
                        }

                        if (!Objects.equals(existingUser.getEmail(), email)) {
                            existingUser.setEmail(email);
                            updated = true;
                        }

                        if (!Objects.equals(existingUser.getUsername(), username)) {
                            existingUser.setUsername(username);
                            updated = true;
                        }

                        if (updated) {
                            userRepository.save(existingUser);
                        }
                    },
                    () -> {
                        User newUser = User.builder()
                                .uuid(uuid)
                                .username(username)
                                .email(email)
                                .roles(currentRoles)
                                .build();
                        userRepository.save(newUser);
                    }
            );

            log.info("KeycloakUserSyncFilter: Keycloak user synchronized");
        }
        filterChain.doFilter(request, response);
    }

}
