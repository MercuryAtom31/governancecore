package com.benzair.governancecore.privilegedaccesssubdomain.datamapperlayer;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.benzair.governancecore.privilegedaccesssubdomain.datalayer.PrivilegedAccess;
import com.benzair.governancecore.privilegedaccesssubdomain.presentationlayer.PrivilegedAccessResponseModel;

@Mapper(componentModel = "spring")
public interface PrivilegedAccessResponseMapper {

    @Mapping(source = "privilegedAccessIdentifier.privilegedAccessId", target = "privilegedAccessId")
    PrivilegedAccessResponseModel entityToResponseModel(PrivilegedAccess privilegedAccess);

    List<PrivilegedAccessResponseModel> entityToResponseModelList(List<PrivilegedAccess> privilegedAccesses);
}
