package com.itechart.springsecuritydemo.service;

import com.itechart.springsecuritydemo.entity.ProfileDeletionRequest;

import java.util.List;
import java.util.UUID;

public interface ProfileDeletionService {

    void deleteProfileDirectly(UUID targetUserUuid, UUID supervisorUuid);

    ProfileDeletionRequest requestProfileDeletion(UUID targetUserUuid, UUID requestedBy);

    List<ProfileDeletionRequest> getPendingRequests();

    ProfileDeletionRequest approveRequest(Long requestId, UUID supervisorUuid);

    ProfileDeletionRequest rejectRequest(Long requestId, UUID supervisorUuid);
}
