package com.usermanagerproj.contracts.admin;

import com.usermanagerproj.contracts.BaseService;
import com.usermanagerproj.domain.user.AppUser;
import com.usermanagerproj.dto.user.request.CreateBasicUserRequest;
import com.usermanagerproj.dto.user.request.UpdateDetailsRequest;
import com.usermanagerproj.dto.user.response.AppUserResponse;

import java.util.List;
import java.util.UUID;

public interface AdminService extends BaseService {
    List<AppUserResponse> fetchAllActiveUsers(int size, int page);
    String createUser(CreateBasicUserRequest createBasicUserRequest);
    AppUser update(UUID uuid, UpdateDetailsRequest updateDetailsRequest);
    String blockUser(String userName);
    String unblockUser(String userName);
    void deleteUser(UUID uuid);
}
