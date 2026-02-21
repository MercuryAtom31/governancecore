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
        
        public AssetServiceImpl (
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

        public AssetResponseModel getAssetByAssetId(String assetId) {
            Asset asset = assetRepository.findByAssetIdentifier_AssetId(assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with assetId: " + assetId));
            return assetResponseMapper.entityToResponseModel(asset);
        }

        public void deleteAssetByAssetId(String assetId) {
            
        }
    }
