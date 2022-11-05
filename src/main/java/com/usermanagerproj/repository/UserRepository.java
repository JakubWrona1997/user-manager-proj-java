package com.usermanagerproj.repository;

import com.usermanagerproj.domain.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<AppUser, UUID> {
    Optional<AppUser> findAppUserByUsername (String username);
    Boolean existsAppUserByUsername(String username);
    Boolean existsAppUserByEmail (String email);
}
