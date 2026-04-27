package com.benzair.governancecore.privilegedaccesssubdomain.presentationlayer;

import com.benzair.governancecore.privilegedaccesssubdomain.datalayer.PrivilegedAccessDuration;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrivilegedAccessRequestModel {

    @NotNull
    private PrivilegedAccessDuration durationMinutes;

    @NotBlank
    private String justification;
}
