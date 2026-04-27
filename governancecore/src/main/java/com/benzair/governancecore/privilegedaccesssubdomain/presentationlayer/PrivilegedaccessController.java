package com.benzair.governancecore.privilegedaccesssubdomain.presentationlayer;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.benzair.governancecore.privilegedaccesssubdomain.businesslayer.PrivilegedAccessService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/privileged-access")
public class PrivilegedAccessController {

    private final PrivilegedAccessService privilegedAccessService;

    public PrivilegedAccessController(PrivilegedAccessService privilegedAccessService) {
        this.privilegedAccessService = privilegedAccessService;
    }

    @GetMapping
    public ResponseEntity<List<PrivilegedAccessResponseModel>> getAllPrivilegedAccesses() {
        return ResponseEntity.ok(privilegedAccessService.getAllPrivilegedAccesses());
    }

    @GetMapping("/{privilegedAccessId}")
    public ResponseEntity<PrivilegedAccessResponseModel> getPrivilegedAccessByPrivilegedAccessId(
            @PathVariable UUID privilegedAccessId) {
        return ResponseEntity.ok(privilegedAccessService.getPrivilegedAccessByPrivilegedAccessId(privilegedAccessId));
    }

    @PostMapping
    public ResponseEntity<PrivilegedAccessResponseModel> addPrivilegedAccess(
            @Valid @RequestBody PrivilegedAccessRequestModel privilegedAccessRequestModel) {
        PrivilegedAccessResponseModel createdPrivilegedAccess =
                privilegedAccessService.addPrivilegedAccess(privilegedAccessRequestModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPrivilegedAccess);
    }

    @PutMapping("/{privilegedAccessId}")
    public ResponseEntity<PrivilegedAccessResponseModel> updatePrivilegedAccess(
            @PathVariable UUID privilegedAccessId,
            @Valid @RequestBody PrivilegedAccessRequestModel privilegedAccessRequestModel) {
        return ResponseEntity.ok(privilegedAccessService.updatePrivilegedAccess(
                privilegedAccessRequestModel,
                privilegedAccessId));
    }

    @DeleteMapping("/{privilegedAccessId}")
    public ResponseEntity<Void> deletePrivilegedAccessByPrivilegedAccessId(@PathVariable UUID privilegedAccessId) {
        privilegedAccessService.deletePrivilegedAccessByPrivilegedAccessId(privilegedAccessId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{privilegedAccessId}/grant")
    public ResponseEntity<PrivilegedAccessResponseModel> grantPrivilegedAccess(@PathVariable UUID privilegedAccessId) {
        return ResponseEntity.ok(privilegedAccessService.grantPrivilegedAccess(privilegedAccessId));
    }

    @PatchMapping("/{privilegedAccessId}/refuse")
    public ResponseEntity<PrivilegedAccessResponseModel> refusePrivilegedAccess(@PathVariable UUID privilegedAccessId) {
        return ResponseEntity.ok(privilegedAccessService.refusePrivilegedAccess(privilegedAccessId));
    }

    @PatchMapping("/{privilegedAccessId}/revoke")
    public ResponseEntity<PrivilegedAccessResponseModel> revokePrivilegedAccess(@PathVariable UUID privilegedAccessId) {
        return ResponseEntity.ok(privilegedAccessService.revokePrivilegedAccess(privilegedAccessId));
    }

    @PatchMapping("/{privilegedAccessId}/in-use")
    public ResponseEntity<PrivilegedAccessResponseModel> markPrivilegedAccessInUse(
            @PathVariable UUID privilegedAccessId) {
        return ResponseEntity.ok(privilegedAccessService.markPrivilegedAccessInUse(privilegedAccessId));
    }

    @PatchMapping("/{privilegedAccessId}/expire")
    public ResponseEntity<PrivilegedAccessResponseModel> expirePrivilegedAccess(@PathVariable UUID privilegedAccessId) {
        return ResponseEntity.ok(privilegedAccessService.expirePrivilegedAccess(privilegedAccessId));
    }
}
