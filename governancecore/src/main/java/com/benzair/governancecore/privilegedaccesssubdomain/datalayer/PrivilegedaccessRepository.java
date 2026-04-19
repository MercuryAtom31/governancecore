package com.benzair.governancecore.privilegedaccesssubdomain.datalayer;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;
import com.benzair.governancecore.assetsubdomain.datalayer.Asset;

public interface PrivilegedAccessRepository extends JpaRepository<PrivilegedAccess, UUID> {
    
    Optional<PrivilegedAccess> findByPrivilegedAccessIdentifier_PrivilegedAccessId(UUID privilegedAccessId);
}
