package com.usermanagerproj.config;

import com.github.javafaker.Faker;
import com.usermanagerproj.domain.role.ERole;
import com.usermanagerproj.domain.role.Role;
import com.usermanagerproj.domain.user.AppUser;
import com.usermanagerproj.repository.RoleRepository;
import com.usermanagerproj.repository.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static com.usermanagerproj.domain.role.ERole.ROLE_ADMIN;
import static com.usermanagerproj.domain.role.ERole.ROLE_USER;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final Faker faker;

    public SetupDataLoader(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, Faker faker) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.faker = faker;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (userRepository.existsAppUserByUsername("Tester"))
            return;

        createRoleIfNotFound(ERole.ROLE_SUPERADMIN);
        createRoleIfNotFound(ROLE_ADMIN);
        createRoleIfNotFound(ERole.ROLE_USER);


        Role userRole = roleRepository.findRoleByName(ROLE_USER);
        createFakeUsers(userRole);

        Role adminRole = roleRepository.findRoleByName(ROLE_ADMIN);
        createUserIfNotExist(adminRole);
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
    void createFakeUsers(Role userRole) {
        List<AppUser> users = IntStream.rangeClosed(1, 100)
                .mapToObj(i -> new AppUser(
                        faker.name().firstName(),
                        faker.name().lastName(),
                        faker.name().username(),
                        faker.internet().emailAddress(),
                        passwordEncoder.encode("password"),
                        faker.number().numberBetween(18, 100),
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        false,
                        true,
                        Collections.singleton(userRole)
                ))
                .toList();

        userRepository.saveAll(users);
    }

    @Transactional
    void createUserIfNotExist(Role adminRole){

        AppUser newUser = new AppUser();
        newUser.setFirstName("Test");
        newUser.setLastName("Test");
        newUser.setUsername("Tester");
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setPassword(passwordEncoder.encode("test"));
        newUser.setEmail("test@test.com");
        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        newUser.setRoles(roles);
        newUser.setIsBlocked(false);
        newUser.setIsEnabled(true);

        if(userRepository.findAppUserByUsername(newUser.getUsername()).isEmpty()){
            userRepository.save(newUser);
        }
    }
}
