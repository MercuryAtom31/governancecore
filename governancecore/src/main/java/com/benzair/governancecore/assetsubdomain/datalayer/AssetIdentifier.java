package com.benzair.governancecore.assetsubdomain.datalayer;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

@Embeddable // JPA annotation to mark this class as an embeddable object, meaning its fields will be stored in the same table as the Asset entity.
@Data
@AllArgsConstructor
public class AssetIdentifier { // AssetIdentifier is a business identifier, not the database ID.
    
    // JPA annotation to specify that this column is named "asset_id", cannot be null, must be unique, and has a maximum length of 36 characters (suitable for UUID strings).
    @Column(name = "asset_id", nullable = false, unique = true)
    private UUID assetId;

    /*
    No-arg constructor generates a random UUID string automatically.
    */
    public AssetIdentifier() {
        // The following means: "generate UUID, then convert it to text."
        this.assetId = java.util.UUID.randomUUID();
    }
}

/*
This class is a value object for external ID (asset_id) that can be embedded into Asset.
A Value Object is a small object that represents a simple entity 
whose equality is not based on identity, 
but rather on the values of its attributes. 

In this case, AssetIdentifier is a value object that encapsulates the assetId, 
which is a unique identifier for an asset. 
It is marked as @Embeddable, meaning it can be embedded into another entity (like Asset) 
and its fields will be stored in the same table as the owning entity. 
The assetId is generated as a UUID string to ensure uniqueness across all assets.

So we now have two identities:

1) Database Identity (internal)
private UUID id;

Used by:

-Hibernate
-Foreign keys
-Internal joins

Frontend should NEVER see this.

2) Business Identity (external)
private AssetIdentifier assetIdentifier;

Used by:

-API responses
-URLs
-Service layer
-Logs
-Auditing

This is what your frontend should use.
*/