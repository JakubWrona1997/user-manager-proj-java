package com.usermanagerproj.service;

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
    public AppUser saveUser(SignUpRequest signUpRequest) {

        AppUser newAppUser = setUpNewUser(new AppUser(), signUpRequest);

        return userRepository.save(newAppUser);
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
    private AppUser setUpNewUser(AppUser appUser, SignUpRequest signUpRequest){
        appUser.setUsername(signUpRequest.getUsername());
        appUser.setEmail(signUpRequest.getEmail());
        appUser.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        appUser.setCreatedAt(LocalDateTime.now());
        appUser.setFirstName(signUpRequest.getFirstName());
        appUser.setLastName(signUpRequest.getLastName());
        appUser.setAge(signUpRequest.getAge());
        appUser.setIsBlocked(false);
        appUser.setIsEnabled(true);

        Role userRole = roleRepository.findRoleByName(ERole.ROLE_USER);
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        appUser.setRoles(roles);

        return appUser;
    }
}

