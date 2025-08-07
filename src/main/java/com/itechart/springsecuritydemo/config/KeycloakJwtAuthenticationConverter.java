package com.itechart.springsecuritydemo.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Object rolesClaim = jwt.getClaim("roles");

        if (!(rolesClaim instanceof List<?> rolesList)) {
            return Collections.emptyList();
        }

        return rolesList.stream()
                .filter(Objects::nonNull)
                .map(Object::toString)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }
}
