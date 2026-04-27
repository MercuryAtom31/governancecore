package com.benzair.governancecore.privilegedaccesssubdomain.datamapperlayer;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.benzair.governancecore.privilegedaccesssubdomain.datalayer.PrivilegedAccess;
import com.benzair.governancecore.privilegedaccesssubdomain.datalayer.PrivilegedAccessRole;
import com.benzair.governancecore.privilegedaccesssubdomain.presentationlayer.PrivilegedAccessRequestModel;

@Mapper(componentModel = "spring")
public interface PrivilegedAccessRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "privilegedAccessIdentifier", ignore = true)
    @Mapping(target = "requesterUserId", ignore = true)
    @Mapping(target = "requesterName", ignore = true)
    @Mapping(target = "requesterCurrentRole", ignore = true)
    @Mapping(target = "role", expression = "java(defaultRole())")
    @Mapping(target = "requestedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "grantedByUserId", ignore = true)
    @Mapping(target = "grantedByName", ignore = true)
    @Mapping(target = "approvalNote", ignore = true)
    @Mapping(target = "grantedAt", ignore = true)
    @Mapping(target = "refusedAt", ignore = true)
    @Mapping(target = "refusalReason", ignore = true)
    @Mapping(target = "expiresAt", ignore = true)
    @Mapping(target = "revokedAt", ignore = true)
    @Mapping(target = "revokeReason", ignore = true)
    PrivilegedAccess requestModelToEntity(PrivilegedAccessRequestModel privilegedAccessRequestModel);

    default PrivilegedAccessRole defaultRole() {
        return PrivilegedAccessRole.ADMIN;
    }
}
