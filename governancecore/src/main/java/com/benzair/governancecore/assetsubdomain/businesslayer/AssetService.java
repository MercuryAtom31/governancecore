package com.benzair.governancecore.assetsubdomain.businesslayer;

import java.util.List;
import com.benzair.governancecore.assetsubdomain.presentationlayer.AssetRequestModel;
import com.benzair.governancecore.assetsubdomain.presentationlayer.AssetResponseModel;

public interface AssetService {
    
    List<AssetResponseModel> getAllAssets();

    AssetResponseModel getAssetByAssetId(String assetId);

    void deleteAssetByAssetId(String assetId);

    AssetResponseModel addAsset(AssetRequestModel assetRequestModel);

    AssetResponseModel updateAsset(String assetId, AssetRequestModel assetRequestModel);

}
