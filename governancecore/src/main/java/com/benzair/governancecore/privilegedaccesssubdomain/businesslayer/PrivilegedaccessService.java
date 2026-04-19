package com.benzair.governancecore.privilegedaccesssubdomain.businesslayer;

import com.benzair.governancecore.privilegedaccesssubdomain.presentationlayer.PrivilegedAccessResponseModel;

import java.util.List;
import java.util.UUID;

public interface PrivilegedAccessService {
    
    List<PrivilegedAccessResponseModel> getAllPrivilegedAccesses();

    PrivilegedAccessResponseModel getPrivilegedAccessByPrivilegedAccessId(UUID privilegedAccessId);

    void deletePrivilegedAccessByPrivilegedAccessId(UUID privilegedAccessId);

    PrivilegedAccessResponseModel addPrivilegedAccess(PrivilegedAccessResponseModel privilegedAccessRequestModel);

    PrivilegedAccessResponseModel updatePrivilegedAccess(PrivilegedAccessResponseModel privilegedAccessRequestModel, UUID privilegedAccessId);
}
