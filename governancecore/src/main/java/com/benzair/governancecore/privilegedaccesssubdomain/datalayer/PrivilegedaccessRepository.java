package com.benzair.governancecore.privilegedaccesssubdomain.datalayer;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegedAccessRepository extends JpaRepository<PrivilegedAccess, UUID> {
    
    Optional<PrivilegedAccess> findByPrivilegedAccessIdentifier_PrivilegedAccessId(UUID privilegedAccessId);
}
