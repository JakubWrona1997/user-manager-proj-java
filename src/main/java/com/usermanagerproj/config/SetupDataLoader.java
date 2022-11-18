package com.usermanagerproj.config;

import com.usermanagerproj.domain.role.ERole;
import com.usermanagerproj.domain.role.Role;
import com.usermanagerproj.domain.user.AppUser;
import com.usermanagerproj.repository.RoleRepository;
import com.usermanagerproj.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static com.usermanagerproj.domain.role.ERole.ROLE_ADMIN;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup)
            return;

        createRoleIfNotFound(ERole.ROLE_SUPERADMIN);
        createRoleIfNotFound(ROLE_ADMIN);
        createRoleIfNotFound(ERole.ROLE_USER);

        Role superAdminRole = roleRepository.findRoleByName(ROLE_ADMIN);
        AppUser newUser = new AppUser();
        newUser.setFirstName("Test");
        newUser.setLastName("Test");
        newUser.setUsername("Tester");
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setPassword(passwordEncoder.encode("test"));
        newUser.setEmail("test@test.com");
        Set<Role> roles = new HashSet<>();
        roles.add(superAdminRole);
        newUser.setRoles(roles);
        newUser.setIsBlocked(false);
        newUser.setIsEnabled(true);

        createUserIfNotExist(newUser);

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

    @Transactional
    void createUserIfNotExist(AppUser user){

        if(userRepository.findAppUserByUsername(user.getUsername()).isEmpty()){
            userRepository.save(user);
        }
    }
}
