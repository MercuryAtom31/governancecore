package com.benzair.governancecore.assetsubdomain.datalayer;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.Optional;

public interface AssetRepository extends  JpaRepository<Asset, UUID> {
    // JpaRepository provides basic CRUD operations and pagination for Asset entity.

    Optional<Asset> findByAssetIdentifier_AssetId(String assetId);
    // Custom query method to find an Asset by its business ID (external uuid) using the embedded AssetIdentifier.
    
}
