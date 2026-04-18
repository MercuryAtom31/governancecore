package com.benzair.governancecore.privilegedaccesssubdomain.datalayer;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

@Embeddable
@Data
@AllArgsConstructor
public class PrivilegedAccessIdentifier {
    
    @Column(name = "privileged_access_id", nullable = false, unique = true)
    private String privilegedAccessId;

    public PrivilegedAccessIdentifier() {
        this.privilegedAccessId = UUID.randomUUID();
    }
}
