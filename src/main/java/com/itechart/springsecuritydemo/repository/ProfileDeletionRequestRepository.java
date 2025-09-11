package com.itechart.springsecuritydemo.repository;


import com.itechart.springsecuritydemo.entity.ProfileDeletionRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfileDeletionRequestRepository extends JpaRepository<ProfileDeletionRequest, Long> {
    List<ProfileDeletionRequest> findByStatus(ProfileDeletionRequest.Status status);
}
