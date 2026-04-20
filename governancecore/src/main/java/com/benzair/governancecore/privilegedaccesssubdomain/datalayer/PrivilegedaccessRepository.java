package com.benzair.governancecore.privilegedaccesssubdomain.datalayer;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegedAccessRepository extends JpaRepository<PrivilegedAccess, UUID> {
    
    Optional<PrivilegedAccess> findByPrivilegedAccessIdentifier_PrivilegedAccessId(String privilegedAccessId);

    List<PrivilegedAccess> findByStatus(PrivilegedAccessStatus status);

    List<PrivilegedAccess> findByRequesterUserIdOrderByRequestedAtDesc(String requesterUserId);

    List<PrivilegedAccess> findByRequesterUserIdAndStatusInAndExpiresAtAfter(
            String requesterUserId,
            List<PrivilegedAccessStatus> statuses,
            Instant now);
}
