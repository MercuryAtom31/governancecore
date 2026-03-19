package com.benzair.governancecore.authsubdomain.presentationlayer;

import java.util.List;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AuthUserResponseModel {
    String username;
    String email;
    List<String> roles;
}
