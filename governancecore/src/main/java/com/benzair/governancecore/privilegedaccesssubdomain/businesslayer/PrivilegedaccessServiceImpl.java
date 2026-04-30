package com.benzair.governancecore.privilegedaccesssubdomain.businesslayer;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import com.benzair.governancecore.privilegedaccesssubdomain.datalayer.PrivilegedAccess;
import com.benzair.governancecore.privilegedaccesssubdomain.datalayer.PrivilegedAccessRole;
import com.benzair.governancecore.privilegedaccesssubdomain.datalayer.PrivilegedAccessRepository;
import com.benzair.governancecore.privilegedaccesssubdomain.datalayer.PrivilegedAccessStatus;
import com.benzair.governancecore.privilegedaccesssubdomain.datamapperlayer.PrivilegedAccessRequestMapper;
import com.benzair.governancecore.privilegedaccesssubdomain.datamapperlayer.PrivilegedAccessResponseMapper;
import com.benzair.governancecore.privilegedaccesssubdomain.presentationlayer.PrivilegedAccessRequestModel;
import com.benzair.governancecore.privilegedaccesssubdomain.presentationlayer.PrivilegedAccessResponseModel;
import com.benzair.governancecore.utils.exceptions.InvalidInputException;
import com.benzair.governancecore.utils.exceptions.ResourceNotFoundException;

@Service
public class PrivilegedAccessServiceImpl implements PrivilegedAccessService {

    private final PrivilegedAccessRepository privilegedAccessRepository;
    private final PrivilegedAccessRequestMapper privilegedAccessRequestMapper;
    private final PrivilegedAccessResponseMapper privilegedAccessResponseMapper;

    public PrivilegedAccessServiceImpl(
            PrivilegedAccessRepository privilegedAccessRepository,
            PrivilegedAccessRequestMapper privilegedAccessRequestMapper,
            PrivilegedAccessResponseMapper privilegedAccessResponseMapper) {
        this.privilegedAccessRepository = privilegedAccessRepository;
        this.privilegedAccessRequestMapper = privilegedAccessRequestMapper;
        this.privilegedAccessResponseMapper = privilegedAccessResponseMapper;
    }

    @Override
    public List<PrivilegedAccessResponseModel> getAllPrivilegedAccesses() {
        return privilegedAccessResponseMapper.entityToResponseModelList(privilegedAccessRepository.findAll());
    }

    @Override
    public PrivilegedAccessResponseModel getPrivilegedAccessByPrivilegedAccessId(UUID privilegedAccessId) {
        return privilegedAccessResponseMapper.entityToResponseModel(findByPrivilegedAccessId(privilegedAccessId));
    }

    @Override
    public void deletePrivilegedAccessByPrivilegedAccessId(UUID privilegedAccessId) {
        privilegedAccessRepository.delete(findByPrivilegedAccessId(privilegedAccessId));
    }

    @Override
    public PrivilegedAccessResponseModel addPrivilegedAccess(PrivilegedAccessRequestModel privilegedAccessRequestModel) {
        validateCreateOrUpdateRequest(privilegedAccessRequestModel);
        CurrentUserContext currentUser = getCurrentUserContext();
        PrivilegedAccess privilegedAccess = privilegedAccessRequestMapper.requestModelToEntity(privilegedAccessRequestModel);
        privilegedAccess.setRequesterUserId(currentUser.userId());
        privilegedAccess.setRequesterFirstName(currentUser.firstName());
        privilegedAccess.setRequesterLastName(currentUser.lastName());
        privilegedAccess.setRequesterCurrentRole(currentUser.currentRole());
        privilegedAccess.setRole(PrivilegedAccessRole.ADMIN);
        PrivilegedAccess savedPrivilegedAccess = privilegedAccessRepository.save(privilegedAccess);
        return privilegedAccessResponseMapper.entityToResponseModel(savedPrivilegedAccess);
    }

    @Override
    public PrivilegedAccessResponseModel updatePrivilegedAccess(
            PrivilegedAccessRequestModel privilegedAccessRequestModel,
            UUID privilegedAccessId) {
        validateCreateOrUpdateRequest(privilegedAccessRequestModel);
        CurrentUserContext currentUser = getCurrentUserContext();
        PrivilegedAccess existingPrivilegedAccess = findByPrivilegedAccessId(privilegedAccessId);
        requireStatus(existingPrivilegedAccess, PrivilegedAccessStatus.REQUESTED, "Only requested access can be updated.");

        existingPrivilegedAccess.setRequesterUserId(currentUser.userId());
        existingPrivilegedAccess.setRequesterFirstName(currentUser.firstName());
        existingPrivilegedAccess.setRequesterLastName(currentUser.lastName());
        existingPrivilegedAccess.setRequesterCurrentRole(currentUser.currentRole());
        existingPrivilegedAccess.setRole(PrivilegedAccessRole.ADMIN);
        existingPrivilegedAccess.setDurationMinutes(privilegedAccessRequestModel.getDurationMinutes());
        existingPrivilegedAccess.setJustification(privilegedAccessRequestModel.getJustification());

        return privilegedAccessResponseMapper.entityToResponseModel(
                privilegedAccessRepository.save(existingPrivilegedAccess));
    }

    @Override
    public PrivilegedAccessResponseModel grantPrivilegedAccess(UUID privilegedAccessId) {
        CurrentUserContext currentUser = getCurrentUserContext();
        PrivilegedAccess privilegedAccess = findByPrivilegedAccessId(privilegedAccessId);
        requireStatus(privilegedAccess, PrivilegedAccessStatus.REQUESTED, "Only requested access can be granted.");

        Instant grantedAt = Instant.now();
        privilegedAccess.setGrantedByUserId(currentUser.userId());
        privilegedAccess.setGrantedByFirstName(currentUser.firstName());
        privilegedAccess.setGrantedByLastName(currentUser.lastName());
        privilegedAccess.setApprovalNote(null);
        privilegedAccess.setGrantedAt(grantedAt);
        privilegedAccess.setExpiresAt(grantedAt.plusSeconds(privilegedAccess.getDurationMinutes().getMinutes() * 60L));
        privilegedAccess.setStatus(PrivilegedAccessStatus.GRANTED);

        return privilegedAccessResponseMapper.entityToResponseModel(privilegedAccessRepository.save(privilegedAccess));
    }

    @Override
    public PrivilegedAccessResponseModel refusePrivilegedAccess(UUID privilegedAccessId) {
        PrivilegedAccess privilegedAccess = findByPrivilegedAccessId(privilegedAccessId);
        requireStatus(privilegedAccess, PrivilegedAccessStatus.REQUESTED, "Only requested access can be refused.");

        privilegedAccess.setRefusedAt(Instant.now());
        privilegedAccess.setRefusalReason(null);
        privilegedAccess.setStatus(PrivilegedAccessStatus.REFUSED);

        return privilegedAccessResponseMapper.entityToResponseModel(privilegedAccessRepository.save(privilegedAccess));
    }

    @Override
    public PrivilegedAccessResponseModel revokePrivilegedAccess(UUID privilegedAccessId) {
        PrivilegedAccess privilegedAccess = findByPrivilegedAccessId(privilegedAccessId);
        if (privilegedAccess.getStatus() != PrivilegedAccessStatus.GRANTED
                && privilegedAccess.getStatus() != PrivilegedAccessStatus.IN_USE) {
            throw new InvalidInputException("Only granted or in-use access can be revoked.");
        }

        privilegedAccess.setRevokedAt(Instant.now());
        privilegedAccess.setRevokeReason(null);
        privilegedAccess.setStatus(PrivilegedAccessStatus.REVOKED);

        return privilegedAccessResponseMapper.entityToResponseModel(privilegedAccessRepository.save(privilegedAccess));
    }

    @Override
    public PrivilegedAccessResponseModel markPrivilegedAccessInUse(UUID privilegedAccessId) {
        PrivilegedAccess privilegedAccess = findByPrivilegedAccessId(privilegedAccessId);
        requireStatus(privilegedAccess, PrivilegedAccessStatus.GRANTED, "Only granted access can be marked in use.");

        privilegedAccess.setStatus(PrivilegedAccessStatus.IN_USE);

        return privilegedAccessResponseMapper.entityToResponseModel(privilegedAccessRepository.save(privilegedAccess));
    }

    @Override
    public PrivilegedAccessResponseModel expirePrivilegedAccess(UUID privilegedAccessId) {
        PrivilegedAccess privilegedAccess = findByPrivilegedAccessId(privilegedAccessId);
        if (privilegedAccess.getStatus() != PrivilegedAccessStatus.GRANTED
                && privilegedAccess.getStatus() != PrivilegedAccessStatus.IN_USE) {
            throw new InvalidInputException("Only granted or in-use access can be expired.");
        }

        privilegedAccess.setStatus(PrivilegedAccessStatus.EXPIRED);
        if (privilegedAccess.getExpiresAt() == null) {
            privilegedAccess.setExpiresAt(Instant.now());
        }

        return privilegedAccessResponseMapper.entityToResponseModel(privilegedAccessRepository.save(privilegedAccess));
    }

    private PrivilegedAccess findByPrivilegedAccessId(UUID privilegedAccessId) {
        return privilegedAccessRepository.findByPrivilegedAccessIdentifier_PrivilegedAccessId(privilegedAccessId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Privileged access not found with privilegedAccessId: " + privilegedAccessId));
    }

    private void requireStatus(
            PrivilegedAccess privilegedAccess,
            PrivilegedAccessStatus expectedStatus,
            String errorMessage) {
        if (privilegedAccess.getStatus() != expectedStatus) {
            throw new InvalidInputException(errorMessage);
        }
    }

    private void validateCreateOrUpdateRequest(PrivilegedAccessRequestModel privilegedAccessRequestModel) {
        if (privilegedAccessRequestModel.getDurationMinutes() == null) {
            throw new InvalidInputException("durationMinutes is required.");
        }
        requireText(privilegedAccessRequestModel.getJustification(), "justification is required.");
    }

    private void requireText(String value, String errorMessage) {
        if (value == null || value.isBlank()) {
            throw new InvalidInputException(errorMessage);
        }
    }

    private CurrentUserContext getCurrentUserContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new InvalidInputException("Authenticated user context is required.");
        }

        String userId = firstNonBlank(jwt.getSubject(), jwt.getClaimAsString("preferred_username"));
        String displayName = firstNonBlank(
            jwt.getClaimAsString("name"),
            jwt.getClaimAsString("preferred_username"),
            jwt.getClaimAsString("email"),
            userId);
        NameParts parsedNameParts = parseNameParts(displayName);
        String firstName = firstNonBlank(
            jwt.getClaimAsString("given_name"),
            jwt.getClaimAsString("first_name"),
            parsedNameParts.firstName(),
            displayName);
        String lastName = firstNonBlank(
            jwt.getClaimAsString("family_name"),
            jwt.getClaimAsString("last_name"),
            parsedNameParts.lastName(),
            firstName);
        String currentRole = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority.startsWith("ROLE_"))
                .map(authority -> authority.replace("ROLE_", ""))
                .sorted()
                .collect(Collectors.joining(", "));

        if (isBlank(userId)) {
            throw new InvalidInputException("Authenticated user ID is missing from the token.");
        }
        if (isBlank(firstName)) {
            throw new InvalidInputException("Authenticated user first name is missing from the token.");
        }
        if (isBlank(lastName)) {
            throw new InvalidInputException("Authenticated user last name is missing from the token.");
        }
        if (isBlank(currentRole)) {
            throw new InvalidInputException("Authenticated user role is missing from the token.");
        }

        return new CurrentUserContext(userId, firstName, lastName, currentRole);
    }

    private NameParts parseNameParts(String fullName) {
        if (isBlank(fullName)) {
            return new NameParts(null, null);
        }

        String trimmed = fullName.trim();
        int firstSpaceIndex = trimmed.indexOf(' ');
        if (firstSpaceIndex < 0) {
            return new NameParts(trimmed, null);
        }

        String firstName = trimmed.substring(0, firstSpaceIndex).trim();
        String lastName = trimmed.substring(firstSpaceIndex + 1).trim();
        return new NameParts(firstName, isBlank(lastName) ? null : lastName);
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (!isBlank(value)) {
                return value;
            }
        }
        return null;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private record CurrentUserContext(String userId, String firstName, String lastName, String currentRole) {
    }

    private record NameParts(String firstName, String lastName) {
    }
}
