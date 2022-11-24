package com.usermanagerproj.service.user;

import com.github.javafaker.App;
import com.usermanagerproj.contracts.user.UserService;
import com.usermanagerproj.domain.role.ERole;
import com.usermanagerproj.domain.role.Role;
import com.usermanagerproj.domain.user.AppUser;
import com.usermanagerproj.dto.user.request.ChangePasswordRequest;
import com.usermanagerproj.dto.user.request.SignUpRequest;
import com.usermanagerproj.dto.user.response.AppUserResponse;
import com.usermanagerproj.exception.EntityNotFoundException;
import com.usermanagerproj.repository.RoleRepository;
import com.usermanagerproj.repository.UserRepository;
import com.usermanagerproj.service.registration.token.ConfirmationToken;
import com.usermanagerproj.service.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AppUserResponse fetchUser(UUID uuid) {
        Optional<AppUser> appUser = userRepository.findById(uuid);
        try {
            return unwrapAppUser(appUser, uuid);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AppUserResponse fetchUser(String username) {
        return null;
    }

    @Override
    public String saveUser(AppUser appUser) {

        AppUser newAppUser = setUpNewUser(appUser);

        userRepository.save(newAppUser);

        return createToken(newAppUser);
    }

    private String createToken(AppUser newAppUser) {
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                newAppUser
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;
    }

    @Override
    public int enableAppUser(String email) {
        return userRepository.enableAppUser(email);
    }

    @Override
    public AppUser changePassword(UUID uuid, ChangePasswordRequest changePasswordRequest) {
        return null;
    }

    private AppUserResponse unwrapAppUser(Optional<AppUser> appUser, UUID uuid) throws IOException {
        if(appUser.isPresent()){
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper.map(appUser.get(), AppUserResponse.class);
        }
        else throw new EntityNotFoundException(uuid, AppUser.class);
    }
    private AppUser setUpNewUser(AppUser appUserRequest){
        AppUser newAppUser = new AppUser();
        newAppUser.setUsername(appUserRequest.getUsername());
        newAppUser.setEmail(appUserRequest.getEmail());
        newAppUser.setPassword(passwordEncoder.encode(appUserRequest.getPassword()));
        newAppUser.setCreatedAt(LocalDateTime.now());
        newAppUser.setFirstName(appUserRequest.getFirstName());
        newAppUser.setLastName(appUserRequest.getLastName());
        newAppUser.setAge(appUserRequest.getAge());
        newAppUser.setIsBlocked(false);
        newAppUser.setIsEnabled(false);

        Role userRole = roleRepository.findRoleByName(ERole.ROLE_USER);
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        newAppUser.setRoles(roles);

        return newAppUser;
    }
}

