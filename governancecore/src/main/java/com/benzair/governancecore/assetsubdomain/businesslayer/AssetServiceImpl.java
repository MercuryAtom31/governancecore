package com.benzair.governancecore.assetsubdomain.businesslayer;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.benzair.governancecore.assetsubdomain.presentationlayer.AssetRequestModel;
import com.benzair.governancecore.assetsubdomain.presentationlayer.AssetResponseModel;
import com.benzair.governancecore.assetsubdomain.datalayer.Asset;
import com.benzair.governancecore.assetsubdomain.datalayer.AssetIdentifier;
import com.benzair.governancecore.assetsubdomain.datamapperlayer.AssetRequestMapper;
import com.benzair.governancecore.assetsubdomain.datamapperlayer.AssetResponseMapper;
import com.benzair.governancecore.assetsubdomain.datalayer.AssetRepository;
import com.benzair.governancecore.exceptions.ResourceNotFoundException;

@Service
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;
    private final AssetRequestMapper assetRequestMapper;
    private final AssetResponseMapper assetResponseMapper;

    public AssetServiceImpl(AssetRepository assetRepository, AssetRequestMapper assetRequestMapper, AssetResponseMapper assetResponseMapper) {
        this.assetRepository = assetRepository;
        this.assetRequestMapper = assetRequestMapper;
        this.assetResponseMapper = assetResponseMapper;
    }

    @Override
    public List<AssetResponseModel> getAllAssets() {
        List<Asset> assets = assetRepository.findAll();
        return assetResponseMapper.entityToResponseModelList(assets);
    }

    @Override
    public AssetResponseModel getAssetByAssetId(String assetId) {
        Asset asset = assetRepository.findByAssetIdentifier_AssetId(assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + assetId));
        return assetResponseMapper.entityToResponseModel(asset);
    }

    @Override
    public void deleteAssetByAssetId(String assetId) {
        Asset asset = assetRepository.findByAssetIdentifier_AssetId(assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + assetId));
        assetRepository.delete(asset);
    }

    @Override
    public AssetResponseModel addAsset(AssetRequestModel assetRequestModel) {
        Asset asset = assetRequestMapper.requestModelToEntity(assetRequestModel);
        // Generate a unique identifier for the new asset
        String generatedAssetId = UUID.randomUUID().toString();
        AssetIdentifier assetIdentifier = new AssetIdentifier(generatedAssetId);
        asset.setAssetIdentifier(assetIdentifier);
        Asset savedAsset = assetRepository.save(asset);
        return assetResponseMapper.entityToResponseModel(savedAsset);
    }

    @Override
    public AssetResponseModel updateAsset(String assetId, AssetRequestModel assetRequestModel) {
        Asset existingAsset = assetRepository.findByAssetIdentifier_AssetId(assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + assetId));
        
        // Update the existing asset's fields based on the request model
        existingAsset.setName(assetRequestModel.getName());
        existingAsset.setDescription(assetRequestModel.getDescription());
        existingAsset.setType(assetRequestModel.getType());
        existingAsset.setDataClassification(assetRequestModel.getDataClassification());
        Asset updatedAsset = assetRepository.save(existingAsset);
        return assetResponseMapper.entityToResponseModel(updatedAsset);
    }
