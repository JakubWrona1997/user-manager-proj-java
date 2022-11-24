package com.usermanagerproj.contracts;

import com.usermanagerproj.dto.user.response.AppUserResponse;

import java.util.UUID;

public interface BaseService {
    AppUserResponse fetchUser(UUID uuid);
    AppUserResponse fetchUser(String username);
}
