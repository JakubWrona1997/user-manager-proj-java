package com.usermanagerproj.service;

import com.usermanagerproj.contracts.admin.AdminService;
import com.usermanagerproj.domain.role.Role;
import com.usermanagerproj.domain.user.AppUser;
import com.usermanagerproj.dto.user.request.CreateBasicUserRequest;
import com.usermanagerproj.dto.user.request.SignUpRequest;
import com.usermanagerproj.dto.user.request.UpdateDetailsRequest;
import com.usermanagerproj.dto.user.response.AppUserResponse;
import com.usermanagerproj.exception.EntityNotFoundException;
import com.usermanagerproj.repository.RoleRepository;
import com.usermanagerproj.repository.UserRepository;
import com.usermanagerproj.service.registration.token.ConfirmationToken;
import com.usermanagerproj.service.registration.token.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;
    private final ConfirmationTokenService confirmationTokenService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public AppUserResponse fetchUser(UUID uuid) {
        Optional<AppUser> appUser = userRepository.findById(uuid);
        try {
            return unwrapAppUser(appUser, appUser.get().getId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AppUserResponse fetchUser(String username) {
        Optional<AppUser> appUser = userRepository.findAppUserByUsername(username);
        try {
            return unwrapAppUser(appUser, appUser.get().getId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String createUser(CreateBasicUserRequest createBasicUserRequest) {

        AppUser newAppUser = setUpNewUser(new AppUser(), createBasicUserRequest);
        userRepository.save(newAppUser);

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
    public AppUser update(UUID uuid, UpdateDetailsRequest updateDetailsRequest) {
        return null;
    }

    @Override
    public void deleteUser(UUID uuid) {
        try {
            userRepository.deleteById(uuid);
        } catch (Exception e) {
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
    private AppUser setUpNewUser(AppUser appUser, CreateBasicUserRequest createBasicUserRequest){

        appUser.setUsername(createBasicUserRequest.getUsername());
        appUser.setEmail(createBasicUserRequest.getEmail());
        appUser.setPassword(passwordEncoder.encode(generateRandomPassword()));
        appUser.setCreatedAt(LocalDateTime.now());
        appUser.setFirstName(createBasicUserRequest.getFirstName());
        appUser.setLastName(createBasicUserRequest.getLastName());
        appUser.setAge(createBasicUserRequest.getAge());
        appUser.setIsBlocked(false);
        appUser.setIsEnabled(false);
        Role role =  roleRepository.findRoleByName(createBasicUserRequest.getRole());
        appUser.setRoles(Collections.singleton(role));

        return appUser;
    }
    private String generateRandomPassword(){
        return new Random().ints(10, 33, 122).mapToObj(i -> String.valueOf((char)i)).collect(Collectors.joining());
    }
}
