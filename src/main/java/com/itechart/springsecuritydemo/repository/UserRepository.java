package com.itechart.springsecuritydemo.repository;

import com.itechart.springsecuritydemo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAll();

    Optional<User> findByUuid(UUID uuid);

    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);

    Optional<User> findUserByEmail(String email);

    void deleteByUuid(UUID uuid);

    boolean existsUserByUuid(UUID uuid);


}
