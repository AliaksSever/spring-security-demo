package com.itechart.springsecuritydemo.filters;

import com.itechart.profileserviceapi.enums.Role;
import com.itechart.springsecuritydemo.entity.User;
import com.itechart.springsecuritydemo.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.oauth2.jwt.Jwt;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class KeycloakUserSyncFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            Jwt jwt = jwtAuth.getToken();
            String keycloakId = jwt.getSubject();
            String username = jwt.getClaim("preferred_username");
            String email = jwt.getClaim("email");
            List<String> roles = jwt.getClaimAsStringList("roles");

            Set<Role> currentRoles = roles.stream()
                    .filter(role -> role!=null && role.startsWith("ROLE_"))
                    .map(Role::valueOf)
                    .collect(Collectors.toSet());
            UUID uuid = UUID.fromString(keycloakId);

            if (!userRepository.existsUserByUuid(uuid)&& !userRepository.existsUserByUsername(username)) {
                User user = User.builder()
                        .uuid(uuid)
                        .username(username)
                        .email(email)
                        .roles(currentRoles)
                        .build();
                userRepository.save(user);
            }
        }

        filterChain.doFilter(request, response);

    }
}
