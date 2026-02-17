package com.benzair.governancecore.assetsubdomain.presentationlayer;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Lombok annotation to generate getters, setters, toString, equals, and hashCode methods
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class AssetRequestModel {
    
    // Validation annotation to ensure that the name field is not blank
    @NotBlank(message = "Name is required") 
    private String name;

    @NotBlank(message = "Owner is required")
    private String owner;

    @NotBlank(message = "Classification is required")
    private String classification;
}

/**
    AssetRequestModel represents the user's input payload 
    (the data sent from the UI to your backend in the HTTP request body.) 
    when they create or update an asset in our ISMS.

    In ISMS terms, it is the "new/edited asset form data":

    -asset name
    -owner
    -classification
*/