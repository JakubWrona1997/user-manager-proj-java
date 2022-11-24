package com.usermanagerproj.repository;

import com.usermanagerproj.domain.user.AppUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<AppUser, UUID> {
    Optional<AppUser> findAppUserByUsername (String username);
    List<AppUser> findAllByIsEnabled(Boolean isEnabled, Pageable pageable);
    Boolean existsAppUserByUsername(String username);
    Boolean existsAppUserByEmail (String email);
}
