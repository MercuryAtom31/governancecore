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
import jakarta.persistence.PreUpdate;
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
    
    @Column (nullable = false)
    private String name;
    
    @Column (nullable = false)
    private String user;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "entity_type")
    private EntityType entityType;

    @Column (nullable = false)
    private String entityId;

    @Column (nullable = false, updatable = false, name = "utc_timestamp")
    private Instant utcTimestamp;
    
    // The outcome of the audited action (e.g., SUCCESS, FAILURE, DENIED)
    @Enumerated(EnumType.STRING) // This annotation is used when you want to store the enum as a string in the database.
    @Column
    private AuditOutcome outcome;
    
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