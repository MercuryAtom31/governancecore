package com.benzair.governancecore.assetsubdomain.datamapperlayer;

import com.benzair.governancecore.assetsubdomain.presentationlayer.AssetRequestModel;
import com.benzair.governancecore.assetsubdomain.datalayer.Asset;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
/*
AssetRequestMapper
Responsibility:
Convert incoming request into an Asset entity for service save/update flow.
*/
@Mapper(componentModel = "spring")
public interface AssetRequestMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "assetIdentifier", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Asset requestModelToEntity(AssetRequestModel assetRequestModel);
}
