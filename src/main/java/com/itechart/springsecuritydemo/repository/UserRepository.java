package com.itechart.springsecuritydemo.repository;

import com.itechart.profileserviceapi.enums.Role;
import com.itechart.springsecuritydemo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAll();

    Optional<User> findByUuid(UUID uuid);

    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findUserByEmail(String email);

    void deleteByUuid(UUID uuid);

    boolean existsUserByUuid(UUID uuid);

    boolean existsUserByUsername(String username);

    Page<User> findDistinctByRolesIn(Set<Role> roles, Pageable pageable);

}
