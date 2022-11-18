package com.usermanagerproj.contracts.admin;

import com.usermanagerproj.domain.user.AppUser;
import com.usermanagerproj.dto.user.request.CreateBasicUserRequest;
import com.usermanagerproj.dto.user.request.UpdateDetailsRequest;
import com.usermanagerproj.dto.user.response.AppUserResponse;

import java.util.UUID;

public interface AdminService {
    AppUserResponse fetchUser(UUID uuid);
    AppUserResponse fetchUser(String username);
    String createUser(CreateBasicUserRequest createBasicUserRequest);
    AppUser update(UUID uuid, UpdateDetailsRequest updateDetailsRequest);
    void deleteUser(UUID uuid);
}
