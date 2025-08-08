package com.itechart.springsecuritydemo.filters;

import com.itechart.springsecuritydemo.dto.RegisterRequest;
import com.itechart.springsecuritydemo.entity.Role;
import com.itechart.springsecuritydemo.entity.User;
import com.itechart.springsecuritydemo.repository.UserRepository;
import com.itechart.springsecuritydemo.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.oauth2.jwt.Jwt;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

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

            String currentRole = null;
            for (String role : roles) {
                if (role.equals("SUPERVISOR") || role.equals("USER")) {
                    currentRole = role;
                }
            }

            UUID uuid = UUID.fromString(keycloakId);

            if (!userRepository.existsUserByUuid(uuid)) {
                User user = User.builder()
                        .uuid(uuid)
                        .username(username)
                        .email(email)
                        .role(Role.valueOf(currentRole))
                        .password("test123")
                        .build();
                userRepository.save(user);
            }
        }

        filterChain.doFilter(request, response);

    }
}
