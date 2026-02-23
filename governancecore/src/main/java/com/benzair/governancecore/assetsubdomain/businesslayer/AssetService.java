package com.benzair.governancecore.assetsubdomain.businesslayer;

import java.util.List;
import com.benzair.governancecore.assetsubdomain.presentationlayer.AssetRequestModel;
import com.benzair.governancecore.assetsubdomain.presentationlayer.AssetResponseModel;

public interface AssetService {
    
    List<AssetResponseModel> getAllAssets();

    AssetResponseModel getAssetByAssetId(UUID assetId);

    void deleteAssetByAssetId(UUID assetId);

    AssetResponseModel addAsset(AssetRequestModel assetRequestModel);

    AssetResponseModel updateAsset(UUID assetId, AssetRequestModel assetRequestModel);

}
