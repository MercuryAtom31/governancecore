package com.benzair.governancecore.assetsubdomain.datalayer;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Embeddable // JPA annotation to mark this class as an embeddable object, meaning its fields will be stored in the same table as the Asset entity.
@Data
@AllArgsConstructor
public class AssetIdentifier {
    
    // JPA annotation to specify that this column is named "asset_id", cannot be null, must be unique, and has a maximum length of 36 characters (suitable for UUID strings).
    @Column(name = "asset_id", nullable = false, unique = true, length = 36)
    private String assetId;

    /*
    No-arg constructor generates a random UUID string automatically.
    */
    public AssetIdentifier() {
        this.assetId = java.util.UUID.randomUUID().toString();
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
*/