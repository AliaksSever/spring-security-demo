package com.itechart.springsecuritydemo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Getter
@Builder
public class UserDetailsDto implements UserDetails {

   private final UserReadDto user;

   public UserDetailsDto(UserReadDto user) {
      this.user = user;
   }

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
      return user.getUsername();
   }

}
