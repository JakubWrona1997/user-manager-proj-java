package com.usermanagerproj.contracts.user;

import com.usermanagerproj.contracts.BaseService;
import com.usermanagerproj.domain.user.AppUser;
import com.usermanagerproj.dto.user.request.ChangePasswordRequest;

import java.util.UUID;

public interface UserService extends BaseService {
    String saveUser(AppUser appUser);
    AppUser changePassword(UUID uuid, ChangePasswordRequest changePasswordRequest);
}
