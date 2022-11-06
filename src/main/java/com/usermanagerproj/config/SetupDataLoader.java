package com.usermanagerproj.config;

import com.usermanagerproj.domain.role.ERole;
import com.usermanagerproj.domain.role.Role;
import com.usermanagerproj.domain.user.AppUser;
import com.usermanagerproj.repository.RoleRepository;
import com.usermanagerproj.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;

import static com.usermanagerproj.domain.role.ERole.ROLE_ADMIN;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup)
            return;

        createRoleIfNotFound(ERole.ROLE_SUPERADMIN);
        createRoleIfNotFound(ROLE_ADMIN);
        createRoleIfNotFound(ERole.ROLE_USER);

        Role adminRole = roleRepository.findRoleByName(ROLE_ADMIN);
        AppUser user = new AppUser();
        user.setFirstName("Test");
        user.setLastName("Test");
        user.setUsername("Tester");
        user.setCreatedAt(LocalDateTime.now());
        user.setPassword(passwordEncoder().encode("test"));
        user.setEmail("test@test.com");
        user.setRoles(Collections.singletonList(adminRole));
        user.setIsBlocked(false);
        userRepository.save(user);

        alreadySetup = true;
    }
    @Transactional
    void createRoleIfNotFound(ERole name) {

        Role role = roleRepository.findRoleByName(name);
        if (role == null) {
            role = new Role(name);
            roleRepository.save(role);
        }
    }
}
