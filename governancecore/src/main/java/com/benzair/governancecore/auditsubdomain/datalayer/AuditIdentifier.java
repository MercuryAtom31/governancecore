package com.benzair.governancecore.auditsubdomain.datalayer;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

@Embeddable
@Data
@AllArgsConstructor
public class AuditIdentifier {
    
    @Column(name = "audit_id", nullable = false, unique = true)
    private UUID auditId;

    public AuditIdentifier() {
        this.auditId = java.util.UUID.randomUUID();
    }
}
