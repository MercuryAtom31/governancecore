package com.benzair.governancecore.privilegedaccesssubdomain.businesslayer;

import com.benzair.governancecore.privilegedaccesssubdomain.presentationlayer.PrivilegedAccessResponseModel;
import com.benzair.governancecore.privilegedaccesssubdomain.presentationlayer.PrivilegedAccessRequestModel;

import java.util.List;
import java.util.UUID;

public interface PrivilegedAccessService {
    
    List<PrivilegedAccessResponseModel> getAllPrivilegedAccesses();

    PrivilegedAccessResponseModel getPrivilegedAccessByPrivilegedAccessId(UUID privilegedAccessId);

    void deletePrivilegedAccessByPrivilegedAccessId(UUID privilegedAccessId);

    PrivilegedAccessResponseModel addPrivilegedAccess(PrivilegedAccessRequestModel privilegedAccessRequestModel);

    PrivilegedAccessResponseModel updatePrivilegedAccess(PrivilegedAccessRequestModel privilegedAccessRequestModel, UUID privilegedAccessId);

    PrivilegedAccessResponseModel grantPrivilegedAccess(UUID privilegedAccessId);

    PrivilegedAccessResponseModel refusePrivilegedAccess(UUID privilegedAccessId);

    PrivilegedAccessResponseModel revokePrivilegedAccess(UUID privilegedAccessId);

    PrivilegedAccessResponseModel markPrivilegedAccessInUse(UUID privilegedAccessId);

    PrivilegedAccessResponseModel expirePrivilegedAccess(UUID privilegedAccessId);
}
