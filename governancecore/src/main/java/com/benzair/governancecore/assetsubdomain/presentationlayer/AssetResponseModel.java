package com.benzair.governancecore.assetsubdomain.presentationlayer;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetResponseModel {

    private UUID id;
    private String name;
    private String owner;
    private String classification;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

/**
 * AssetResponseModel represents the data that our backend sends back 
 * to the UI in the HTTP response body 
 * after the user creates, updates, or retrieves an asset in our ISMS.
 */
