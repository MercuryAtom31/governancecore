package com.benzair.governancecore.assetsubdomain.datamapperlayer;

import java.util.List;
import com.benzair.governancecore.assetsubdomain.presentationlayer.AssetResponseModel;
import com.benzair.governancecore.assetsubdomain.datalayer.Asset;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AssetResponseMapper {
    

    @Mapping(source = "assetIdentifier.assetId", target = "assetId")
    AssetResponseModel entityToResponseModel(Asset asset);

    List<AssetResponseModel> entityToResponseModelList(List<Asset> assets);
}
