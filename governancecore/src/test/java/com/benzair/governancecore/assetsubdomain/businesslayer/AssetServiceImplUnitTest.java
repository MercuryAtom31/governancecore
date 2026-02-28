package com.benzair.governancecore.assetsubdomain.businesslayer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

@ExtendWith(MockitoExtension.class)
class AssetServiceImplUnitTest {

    @Mock
    AssetRepository assetRepository;

    @Mock
    AssetRequestMapper assetRequestMapper;

    @Mock
    AssetResponseMapper assetResponseMapper;

    @InjectMocks
    AssetServiceImpl assetService;

    @Test
    void addAsset_shouldReturnCreatedAssetResponse_whenInputIsValid() {
        // Arrange
        UUID businessId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        AssetRequestModel request = AssetRequestModel.builder()
                .name("Customer API")
                .owner("Platform Team")
                .assetType(AssetType.APPLICATION)
                .classification(DataClassification.CONFIDENTIAL)
                .description("Public-facing API handling customer data")
                .build();

        Asset mappedAsset = Asset.builder()
                .name(request.getName())
                .owner(request.getOwner())
                .assetType(request.getAssetType())
                .classification(request.getClassification())
                .description(request.getDescription())
                .build();

        Asset savedAsset = Asset.builder()
                .id(UUID.randomUUID())
                .assetIdentifier(new AssetIdentifier(businessId))
                .name(request.getName())
                .owner(request.getOwner())
                .assetType(request.getAssetType())
                .classification(request.getClassification())
                .description(request.getDescription())
                .createdAt(now)
                .updatedAt(now)
                .build();

        AssetResponseModel expected = AssetResponseModel.builder()
                .assetId(businessId)
                .name(request.getName())
                .owner(request.getOwner())
                .assetType(request.getAssetType())
                .classification(request.getClassification())
                .description(request.getDescription())
                .createdAt(now)
                .updatedAt(now)
                .build();

        when(assetRequestMapper.requestModelToEntity(request)).thenReturn(mappedAsset);
        when(assetRepository.save(mappedAsset)).thenReturn(savedAsset);
        when(assetResponseMapper.entityToResponseModel(savedAsset)).thenReturn(expected);

        // Act
        AssetResponseModel actual = assetService.addAsset(request);

        // Assert
        assertEquals(expected, actual);
        verify(assetRequestMapper).requestModelToEntity(request);
        verify(assetRepository).save(mappedAsset);
        verify(assetResponseMapper).entityToResponseModel(savedAsset);
    }
}