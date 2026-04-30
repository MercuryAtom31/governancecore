package com.benzair.governancecore.assetsubdomain.businesslayer;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.benzair.governancecore.assetsubdomain.datalayer.Asset;
import com.benzair.governancecore.assetsubdomain.datalayer.AssetIdentifier;
import com.benzair.governancecore.assetsubdomain.datalayer.AssetRepository;
import com.benzair.governancecore.assetsubdomain.datalayer.AssetType;
import com.benzair.governancecore.assetsubdomain.datalayer.DataClassification;
import com.benzair.governancecore.assetsubdomain.datamapperlayer.AssetRequestMapper;
import com.benzair.governancecore.assetsubdomain.datamapperlayer.AssetResponseMapper;
import com.benzair.governancecore.assetsubdomain.presentationlayer.AssetRequestModel;
import com.benzair.governancecore.assetsubdomain.presentationlayer.AssetResponseModel;
/*
This class is a unit test for the AssetServiceImpl class.
It essentially asks: Does the service call the correct methods in the correct order and return the correct result?
*/
/*
This tells JUnit: "Enable Mockito inside this test class." 
Without this, @Mock and @InjectMocks would not work.
*/
@ExtendWith(MockitoExtension.class)
class AssetServiceImplUnitTest {
    // The Mocks: these are fake versions of our dependencies.
    @Mock
    AssetRepository assetRepository;

    @Mock
    AssetRequestMapper assetRequestMapper;

    @Mock
    AssetResponseMapper assetResponseMapper;
    // This tells Mockito: Create a real AssetServiceImpl object and inject the mocks into it.
    // Now the service is real, but its dependencies are fake.
    @InjectMocks
    AssetServiceImpl assetService;

    // Method naming convention: methodName_shouldExpectedBehavior_whenCondition
    @Test
    void addAsset_shouldReturnCreatedAssetResponse_whenInputIsValid() {
        // ARRANGE (Setup Phase): we create all required data.
        /*
        The Arrange phase answers one question:
        "What must exist for this method to run successfully?"
        */
        UUID businessId = UUID.randomUUID();
        Instant now = Instant.now();

        AssetRequestModel request = AssetRequestModel.builder()
                .firstName("Customer")
                .lastName("API")
                .owner("Platform Team")
                .assetType(AssetType.APPLICATION)
                .classification(DataClassification.CONFIDENTIAL)
                .description("Public-facing API handling customer data")
                .build();

        Asset mappedAsset = Asset.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .owner(request.getOwner())
                .assetType(request.getAssetType())
                .classification(request.getClassification())
                .description(request.getDescription())
                .build();

        Asset savedAsset = Asset.builder()
                .id(UUID.randomUUID())
                .assetIdentifier(new AssetIdentifier(businessId))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .owner(request.getOwner())
                .assetType(request.getAssetType())
                .classification(request.getClassification())
                .description(request.getDescription())
                .createdAt(now)
                .updatedAt(now)
                .build();

        AssetResponseModel expected = AssetResponseModel.builder()
                .assetId(businessId)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .owner(request.getOwner())
                .assetType(request.getAssetType())
                .classification(request.getClassification())
                .description(request.getDescription())
                .createdAt(now)
                .updatedAt(now)
                .build();

        /*
        The following are Mockito stubbings: we tell the mocks what to return when certain methods are called with certain arguments.
        */
        when(assetRequestMapper.requestModelToEntity(request)).thenReturn(mappedAsset);
        when(assetRepository.save(mappedAsset)).thenReturn(savedAsset);
        when(assetResponseMapper.entityToResponseModel(savedAsset)).thenReturn(expected);

        // ACT (Execution Phase): we call the method under test.
        AssetResponseModel actual = assetService.addAsset(request);

        // ASSERT (Verification Phase): we check if the result is what we expected and if the correct methods were called.
        assertEquals(expected, actual);// Check if the actual response matches the expected response.
        verify(assetRequestMapper).requestModelToEntity(request);// Verify that the request mapper was called with the correct request.
        verify(assetRepository).save(mappedAsset);// Verify that the repository's save method was called with the mapped asset.
        verify(assetResponseMapper).entityToResponseModel(savedAsset); // Verify that the response mapper was called with the saved asset.
    }

    // Method naming convention: methodName_shouldExpectedBehavior_whenCondition
    @Test
    void getAllAssets_shouldReturnAllAssets(){
        // ARRANGE
        List<Asset> assets = List.of(
                Asset.builder()
                        .firstName("Server")
                        .lastName("1")
                        .owner("IT department")
                        .assetType(AssetType.SERVER)
                        .classification(DataClassification.CONFIDENTIAL)
                        .description("Server 1 handling customer data")
                        .build(),

                Asset.builder()
                        .firstName("Server")
                        .lastName("2")
                        .owner("IT department")
                        .assetType(AssetType.SERVER)
                        .classification(DataClassification.CONFIDENTIAL)
                        .description("Server 2 handling customer data")
                        .build()
        );

        List<AssetResponseModel> assetResponseModels = List.of(
                AssetResponseModel.builder()
                        .firstName("Server")
                        .lastName("1")
                        .owner("IT department")
                        .assetType(AssetType.SERVER)
                        .classification(DataClassification.CONFIDENTIAL)
                        .description("Server 1 handling customer data")
                        .build(),

                AssetResponseModel.builder()
                        .firstName("Server")
                        .lastName("2")
                        .owner("IT department")
                        .assetType(AssetType.SERVER)
                        .classification(DataClassification.CONFIDENTIAL)
                        .description("Server 2 handling customer data")
                        .build()
        );

        when(assetRepository.findAll()).thenReturn(assets);
        when(assetResponseMapper.entityToResponseModelList(assets)).thenReturn(assetResponseModels);

        // ACT
        List<AssetResponseModel> result = assetService.getAllAssets();

        // ASSERT
        /* 
        Assert answers two questions:

        1) Did the method return the correct result?
        2) Did it interact correctly with its dependencies?
        */
        assertEquals(assetResponseModels, result);
        verify(assetRepository).findAll();
        verify(assetResponseMapper).entityToResponseModelList(assets);
    }
}