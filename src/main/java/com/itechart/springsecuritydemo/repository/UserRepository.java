package com.itechart.springsecuritydemo.repository;

import com.itechart.profileserviceapi.enums.Role;
import com.itechart.springsecuritydemo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAll();

    @Query("SELECT u FROM User u WHERE u.uuid = :uuid")
    @EntityGraph(value="User.withRoles", type = EntityGraph.EntityGraphType.LOAD)
    Optional<User> findByUuidWithRoles(@Param("uuid") UUID uuid);

    default Optional<User> findByUuid(UUID uuid) {
        return findByUuidWithRoles(uuid);
    }

    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findUserByEmail(String email);

    void deleteByUuid(UUID uuid);

    boolean existsUserByUuid(UUID uuid);

    boolean existsUserByUsername(String username);

    Page<User> findDistinctByRolesIn(Set<Role> roles, Pageable pageable);

    List<User> findAllByIdIn(Collection<Long> ids);

    List<User> findAllByUuidIn(Collection<UUID> uuids);
}
