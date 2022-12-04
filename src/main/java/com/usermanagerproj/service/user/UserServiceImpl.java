package com.usermanagerproj.service.user;

import com.usermanagerproj.contracts.user.UserService;
import com.usermanagerproj.domain.role.ERole;
import com.usermanagerproj.domain.role.Role;
import com.usermanagerproj.domain.user.AppUser;
import com.usermanagerproj.dto.user.request.ChangePasswordRequest;
import com.usermanagerproj.dto.user.response.AppUserResponse;
import com.usermanagerproj.event.Event;
import com.usermanagerproj.event.EventType;
import com.usermanagerproj.event.NotificationEventPublisher;
import com.usermanagerproj.exception.EntityNotFoundException;
import com.usermanagerproj.repository.RoleRepository;
import com.usermanagerproj.repository.UserRepository;
import com.usermanagerproj.service.registration.token.ConfirmationToken;
import com.usermanagerproj.service.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
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
    private final NotificationEventPublisher notificationEventPublisher;

    @Override
    public AppUserResponse fetchUser(UUID uuid) {
        Optional<AppUser> appUser = userRepository.findById(uuid);
        try {
            return unwrapAppUser(appUser, uuid);
        } catch (IOException e) {
            throw new EntityNotFoundException(uuid, AppUser.class);
        }
    }

    @Override
    public AppUserResponse fetchUser(String username) {
        Optional<AppUser> appUser = userRepository.findAppUserByUsername(username);
        try {
            return unwrapAppUser(appUser, appUser.get().getId());
        } catch (IOException e) {
            throw new EntityNotFoundException(username, AppUser.class);
        }
    }

    @Override
    public String saveUser(AppUser appUser) {

        AppUser newAppUser = setUpNewUser(appUser);

        userRepository.save(newAppUser);

        notificationEventPublisher.publishEvent(new Event("User registered new account", newAppUser.getUsername(), EventType.USER_CREATED));

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
        notificationEventPublisher.publishEvent(new Event("Created token for user", newAppUser.getUsername(), EventType.TOKEN_CREATED));

        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;
    }

    @Override
    public int enableAppUser(String email) {
        return userRepository.enableAppUser(email);
    }

    @Override
    public String changePassword(UUID uuid, ChangePasswordRequest changePasswordRequest) {
        Optional<AppUser> appUser = userRepository.findById(uuid);
        if(appUser.isPresent()) {
            AppUser user = appUser.get();
            if(passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
                user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
                userRepository.save(user);
                notificationEventPublisher.publishEvent(new Event("User changed password", user.getUsername(), EventType.USER_PASSWORD_CHANGED));
                return "Password changed successfully";
            } else {
                throw new BadCredentialsException("Old password is incorrect");
            }
        } else {
            throw new EntityNotFoundException(uuid, AppUser.class);
        }
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

