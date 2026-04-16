package com.benzair.governancecore.auditsubdomain.datalayer;

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
import java.time.Instant;


@Entity
@Table(name = "audits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Embedded
    private AuditIdentifier auditIdentifier;
    
    // The user who performed the action.
    @Column(nullable = false)
    private String actor;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuditAction action;
    
    // The domain type affected (ASSET, RISK, CONTROL, EVIDENCE).
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "entity_type")
    private EntityType entityType;

    // The ID of the specific resource instance.
    @Column(nullable = false)
    private String entityId;

    @Column (nullable = false, updatable = false, name = "utc_timestamp")
    private Instant utcTimestamp;
    
    // The outcome of the audited action (e.g., SUCCESS, FAILURE, DENIED)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuditOutcome outcome;

    // The origin IP address of the request.
    @Column(name = "source_ip", length = 45)
    private String sourceIp;

    @PrePersist
    public void onCreate() {
        if (auditIdentifier == null) {
            auditIdentifier = new AuditIdentifier();
        }
        if (utcTimestamp == null) {
            utcTimestamp = Instant.now();
        }
    }
    
}

/*
Each audit record should answer:
 who did what,
 to which entity,
 when,
 from where,
 with what result,
 and under which request context.
*/
