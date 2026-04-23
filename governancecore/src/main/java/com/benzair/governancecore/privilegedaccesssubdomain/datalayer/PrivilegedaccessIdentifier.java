package com.benzair.governancecore.privilegedaccesssubdomain.datalayer;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Embeddable
@Data
@AllArgsConstructor
public class PrivilegedAccessIdentifier {

    @Column(name = "privileged_access_id", nullable = false, unique = true)
    private UUID privilegedAccessId;

    public PrivilegedAccessIdentifier() {
        this.privilegedAccessId = UUID.randomUUID();
    }
}
