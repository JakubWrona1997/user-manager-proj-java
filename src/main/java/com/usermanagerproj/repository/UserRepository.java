package com.usermanagerproj.repository;

import com.usermanagerproj.domain.user.AppUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<AppUser, UUID> {
    Optional<AppUser> findAppUserByUsername (String username);
    @Query("SELECT u FROM AppUser u WHERE u.isEnabled = ?1")
    List<AppUser> findAllByIsEnabled(Boolean isEnabled, Pageable pageable);
    Boolean existsAppUserByUsername(String username);
    Boolean existsAppUserByEmail (String email);

    @Transactional
    @Modifying
    @Query("UPDATE AppUser a " +
            "SET a.isEnabled = true " +
            "WHERE a.email = ?1")
    int enableAppUser(String email);

    @Transactional
    @Modifying
    @Query("UPDATE AppUser a " +
            "SET a.isEnabled = false " +
            "WHERE a.id = ?1")
    int disableAppUser(UUID uuid);

    @Transactional
    @Modifying
    @Query("UPDATE AppUser a " +
            "SET a.isBlocked = true " +
            "WHERE a.username = ?1")
    int blockUser(String username);

    @Transactional
    @Modifying
    @Query("UPDATE AppUser a " +
            "SET a.isBlocked = false " +
            "WHERE a.username = ?1")
    int unblockUser(String username);
}
