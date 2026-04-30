package com.benzair.governancecore.privilegedaccesssubdomain.datalayer;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "privileged_access")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrivilegedAccess {

    // Internal database primary key.
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Public/business ID that identifies the privileged access.
    @Embedded
    private PrivilegedAccessIdentifier privilegedAccessIdentifier;

    // This is the requester user ID.
    @Column(name = "requester_user_id", nullable = false)
    private String requesterUserId;

    // This is the requester first name at request time.
    @Column(name = "requester_first_name", nullable = false)
    private String requesterFirstName;

    // This is the requester last name at request time.
    @Column(name = "requester_last_name", nullable = false)
    private String requesterLastName;

    // This is the requester's current role at request time.
    @Column(name = "requester_current_role", nullable = false)
    private String requesterCurrentRole;

    // Only ADMIN can be requested in this PIM flow.
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private PrivilegedAccessRole role;

    // Allowed request durations are 15, 30, or 60 minutes.
    @Enumerated(EnumType.STRING)
    @Column(name = "duration_minutes", nullable = false)
    private PrivilegedAccessDuration durationMinutes;

    // Why the requester needs temporary admin access.
    @Column(nullable = false, length = 1000)
    private String justification;

    // This is when the request was created.
    @Column(name = "requested_at", nullable = false, updatable = false)
    private Instant requestedAt;

    // Current lifecycle state of the privileged access request.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrivilegedAccessStatus status;

    // Approver user ID when request is granted.
    @Column(name = "granted_by_user_id")
    private String grantedByUserId;

    // Approver first name when request is granted.
    @Column(name = "granted_by_first_name")
    private String grantedByFirstName;

    // Approver last name when request is granted.
    @Column(name = "granted_by_last_name")
    private String grantedByLastName;

    // Optional note provided by approver.
    @Column(name = "approval_note", length = 1000)
    private String approvalNote;

    // This is when the privileged access becomes active.
    @Column(name = "granted_at")
    private Instant grantedAt;

    // This is when the request was refused.
    @Column(name = "refused_at")
    private Instant refusedAt;

    // Reason provided when a request is refused.
    @Column(name = "refusal_reason", length = 1000)
    private String refusalReason;

    // This is when the privileged access automatically expires.
    @Column(name = "expires_at")
    private Instant expiresAt;

    // This is when the privileged access was manually revoked (if revoked early).
    @Column(name = "revoked_at")
    private Instant revokedAt;

    // Reason provided when privileged access is revoked early.
    @Column(name = "revoke_reason", length = 1000)
    private String revokeReason;

    @PrePersist
    public void onCreate() {
        if (privilegedAccessIdentifier == null) {
            privilegedAccessIdentifier = new PrivilegedAccessIdentifier();
        }
        if (requestedAt == null) {
            requestedAt = Instant.now();
        }
        if (role == null) {
            role = PrivilegedAccessRole.ADMIN;
        }
        if (status == null) {
            status = PrivilegedAccessStatus.REQUESTED;
        }
    }
}
