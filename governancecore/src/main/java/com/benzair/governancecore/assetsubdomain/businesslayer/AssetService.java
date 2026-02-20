package com.benzair.governancecore.assetsubdomain.businesslayer;

public interface AssetService {
    
    List<AssetResponseModel> getAllAssets();

    AssetResponseModel getAssetByAssetId(String assetId);

    void deleteAssetByAssetId(String assetId);

    AssetResponseModel addAsset(AssetRequestModel assetRequestModel);

    AssetResponseModel updateAsset(String assetId, AssetRequestModel assetRequestModel);

}
