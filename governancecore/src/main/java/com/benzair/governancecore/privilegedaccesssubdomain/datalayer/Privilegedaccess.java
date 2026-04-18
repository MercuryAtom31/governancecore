package com.benzair.governancecore.privilegedaccesssubdomain.datalayer;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
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

    // This is the user who is being granted privileged access.
    @Column(name = "target_user_id", nullable = false)
    private String targetUserId;

    // This is the privileged role being granted (for example, ADMIN).
    @Column(name = "role", nullable = false)
    private String role;

    // This is the approver who authorized the privileged access.
    @Column(name = "approved_by", nullable = false)
    private String approvedBy;

    // This is when the privileged access becomes active.
    @Column(name = "granted_at", nullable = false, updatable = false)
    private Instant grantedAt;

    // This is when the privileged access automatically expires.
    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    // This is when the privileged access was manually revoked (if revoked early).
    @Column(name = "revoked_at")
    private Instant revokedAt;

    @PrePersist
    public void onCreate() {
        if (privilegedAccessIdentifier == null) {
            privilegedAccessIdentifier = new PrivilegedAccessIdentifier();
        }
        if (grantedAt == null) {
            grantedAt = Instant.now();
        }
    }
}
