package com.benzair.governancecore.privilegedaccesssubdomain.presentationlayer;

import java.time.Instant;
import java.util.UUID;

import com.benzair.governancecore.privilegedaccesssubdomain.datalayer.PrivilegedAccessDuration;
import com.benzair.governancecore.privilegedaccesssubdomain.datalayer.PrivilegedAccessRole;
import com.benzair.governancecore.privilegedaccesssubdomain.datalayer.PrivilegedAccessStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrivilegedAccessResponseModel {

    private UUID privilegedAccessId;
    private String requesterFirstName;
    private String requesterLastName;
    private String requesterCurrentRole;
    private PrivilegedAccessRole role;
    private PrivilegedAccessDuration durationMinutes;
    private String justification;
    private Instant requestedAt;
    private PrivilegedAccessStatus status;
    private String grantedByFirstName;
    private String grantedByLastName;
    private Instant grantedAt;
    private Instant refusedAt;
    private Instant expiresAt;
    private Instant revokedAt;
}
