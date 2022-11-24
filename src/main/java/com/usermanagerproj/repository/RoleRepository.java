package com.usermanagerproj.repository;

import com.usermanagerproj.domain.role.ERole;
import com.usermanagerproj.domain.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findRoleByName (ERole role);
}
