package com.itechart.springsecuritydemo.dto;

import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Builder
public record UserDetailsDto(UserReadDto user) implements UserDetails {

   public UUID getUuid() {
      return user.getUuid();
   }

   public String getEmail() {
      return user.getEmail();
   }

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      return Collections.singleton(new SimpleGrantedAuthority(user.getRole().name()));
   }

   @Override
   public String getPassword() {
      return user.getPassword();
   }

   @Override
   public String getUsername() {
      return user.getEmail();
   }

}
