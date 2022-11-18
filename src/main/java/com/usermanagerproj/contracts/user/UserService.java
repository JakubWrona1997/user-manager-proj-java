package com.usermanagerproj.contracts.user;

import com.usermanagerproj.domain.user.AppUser;
import com.usermanagerproj.dto.user.request.ChangePasswordRequest;
import com.usermanagerproj.dto.user.request.SignUpRequest;
import com.usermanagerproj.dto.user.response.AppUserResponse;

import java.util.UUID;

public interface UserService {
    AppUserResponse fetchUser(UUID uuid);
    AppUser saveUser(SignUpRequest signUpRequest);
    AppUser changePassword(UUID uuid, ChangePasswordRequest changePasswordRequest);
}
