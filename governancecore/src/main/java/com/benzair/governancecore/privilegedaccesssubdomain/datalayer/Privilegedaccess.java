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

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Embedded
    private PrivilegedAccessIdentifier privilegedAccessIdentifier;

    @Column(name = "target_user_id", nullable = false)
    private String targetUserId;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "approved_by", nullable = false)
    private String approvedBy;

    @Column(name = "granted_at", nullable = false, updatable = false)
    private Instant grantedAt;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

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
