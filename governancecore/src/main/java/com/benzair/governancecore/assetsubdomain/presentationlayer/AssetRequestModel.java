package com.benzair.governancecore.assetsubdomain.presentationlayer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.benzair.governancecore.assetsubdomain.datalayer.AssetType;
import com.benzair.governancecore.assetsubdomain.datalayer.DataClassification;

@Data // Lombok annotation to generate getters, setters, toString, equals, and hashCode methods
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class AssetRequestModel {
    
    // Validation annotation to ensure that the firstName field is not blank
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String owner;

    @NotNull
    private AssetType assetType;

    @NotNull
    private DataClassification classification;

    private String description;

}

/**
    AssetRequestModel represents the user's input payload 
    (the data sent from the UI to your backend in the HTTP request body.) 
    when they create or update an asset in our ISMS.

    In ISMS terms, it is the "new/edited asset form data":

    -asset first name
    -asset last name
    -owner
    -asset type
*/