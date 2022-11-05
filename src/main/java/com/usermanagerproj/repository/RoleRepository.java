package com.usermanagerproj.repository;

import com.usermanagerproj.domain.role.ERole;
import com.usermanagerproj.domain.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findRoleByName (ERole role);
}
