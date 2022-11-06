package com.usermanagerproj.repository;

import com.usermanagerproj.domain.role.ERole;
import com.usermanagerproj.domain.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findRoleByName (ERole role);
}
