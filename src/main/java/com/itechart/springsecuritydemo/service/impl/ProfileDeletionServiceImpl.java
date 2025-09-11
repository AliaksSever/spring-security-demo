package com.itechart.springsecuritydemo.service.impl;


import com.itechart.springsecuritydemo.entity.ProfileDeletionRequest;
import com.itechart.springsecuritydemo.entity.User;
import com.itechart.springsecuritydemo.repository.ProfileDeletionRequestRepository;
import com.itechart.springsecuritydemo.repository.UserRepository;
import com.itechart.springsecuritydemo.service.ProfileDeletionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileDeletionServiceImpl implements ProfileDeletionService {

    private final UserRepository userRepository;
    private final ProfileDeletionRequestRepository requestRepository;


    @Override
    @Transactional
    public void deleteProfileDirectly(UUID targetUserUuid, UUID supervisorUuid) {
        User user = userRepository.findByUuid(targetUserUuid)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        userRepository.delete(user);

        log.info("Supervisor {} deleted user {}", supervisorUuid, targetUserUuid);
    }

    @Override
    @Transactional
    public ProfileDeletionRequest requestProfileDeletion(UUID targetUserUuid, UUID requestedBy) {
        userRepository.findByUuid(targetUserUuid)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        ProfileDeletionRequest request = ProfileDeletionRequest.builder()
                .userUuid(targetUserUuid)
                .requestedBy(requestedBy)
                .status(ProfileDeletionRequest.Status.PENDING)
                .build();

        ProfileDeletionRequest saved = requestRepository.save(request);
        log.info("User {} requested deletion of profile {}", requestedBy, targetUserUuid);

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProfileDeletionRequest> getPendingRequests() {
        return requestRepository.findByStatus(ProfileDeletionRequest.Status.PENDING);
    }

    @Override
    @Transactional
    public ProfileDeletionRequest approveRequest(Long requestId, UUID supervisorUuid) {
        ProfileDeletionRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));

        if (request.getStatus() != ProfileDeletionRequest.Status.PENDING) {
            throw new IllegalStateException("Request already processed");
        }

        User user = userRepository.findByUuid(request.getUserUuid())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        userRepository.delete(user);

        request.setStatus(ProfileDeletionRequest.Status.APPROVED);
        ProfileDeletionRequest saved = requestRepository.save(request);

        log.info("Supervisor {} approved deletion of user {}", supervisorUuid, request.getUserUuid());

        return saved;
    }

    @Override
    @Transactional
    public ProfileDeletionRequest rejectRequest(Long requestId, UUID supervisorUuid) {
        ProfileDeletionRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));

        if (request.getStatus() != ProfileDeletionRequest.Status.PENDING) {
            throw new IllegalStateException("Request already processed");
        }

        request.setStatus(ProfileDeletionRequest.Status.REJECTED);
        ProfileDeletionRequest saved = requestRepository.save(request);

        log.info("Supervisor {} rejected deletion of user {}", supervisorUuid, request.getUserUuid());

        return saved;
    }

}
