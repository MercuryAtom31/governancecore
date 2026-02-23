package com.benzair.governancecore.assetsubdomain.businesslayer;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.benzair.governancecore.assetsubdomain.datalayer.Asset;
import com.benzair.governancecore.assetsubdomain.datalayer.AssetRepository;
import com.benzair.governancecore.assetsubdomain.datamapperlayer.AssetRequestMapper;
import com.benzair.governancecore.assetsubdomain.datamapperlayer.AssetResponseMapper;
import com.benzair.governancecore.assetsubdomain.presentationlayer.AssetRequestModel;
import com.benzair.governancecore.assetsubdomain.presentationlayer.AssetResponseModel;
import com.benzair.governancecore.src.exceptions.ResourceNotFoundException;

@Service
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;
    private final AssetRequestMapper assetRequestMapper;
    private final AssetResponseMapper assetResponseMapper;

    public AssetServiceImpl(
            AssetRepository assetRepository,
            AssetRequestMapper assetRequestMapper,
            AssetResponseMapper assetResponseMapper
    ) {
        this.assetRepository = assetRepository;
        this.assetRequestMapper = assetRequestMapper;
        this.assetResponseMapper = assetResponseMapper;
    }

    public List<AssetResponseModel> getAllAssets() {
        List<Asset> assets = assetRepository.findAll();
        return assetResponseMapper.entityToResponseModelList(assets);
    }

    public AssetResponseModel getAssetByAssetId(UUID assetId) {
        Asset asset = assetRepository.findByAssetIdentifier_AssetId(assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with assetId: " + assetId));
        return assetResponseMapper.entityToResponseModel(asset);
    }

    public void deleteAssetByAssetId(UUID assetId) {
        Asset asset = assetRepository.findByAssetIdentifier_AssetId(assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with assetId: " + assetId));

        assetRepository.delete(asset);
    }

    public AssetResponseModel addAsset(AssetRequestModel request) {
        /*
        asset is the in-memory object before persistence (mapped from request).
        */
        Asset asset = assetRequestMapper.requestModelToEntity(request);
        /*
        savedAsset is the object returned by repository after DB persistence lifecycle.
        Contains final persisted state (generated IDs, timestamps, etc.).
        */
        Asset savedAsset = assetRepository.save(asset);
        return assetResponseMapper.entityToResponseModel(savedAsset);
    }

    public AssetResponseModel updateAsset(AssetRequestModel request, UUID assetId) {
        Asset existingAsset = assetRepository.findByAssetIdentifier_AssetId(assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with assetId: " + assetId));

        // Updating fields of existing asset with the its new content.
        existingAsset.setName(request.getName());
        existingAsset.setOwner(request.getOwner());
        existingAsset.setAssetType(request.getAssetType());
        existingAsset.setClassification(request.getClassification());
        existingAsset.setDescription(request.getDescription());

        Asset savedAsset = assetRepository.save(existingAsset);
        return assetResponseMapper.entityToResponseModel(savedAsset);
    }
}

// The service flow should be:
// request DTO -> entity -> save -> response DTO.