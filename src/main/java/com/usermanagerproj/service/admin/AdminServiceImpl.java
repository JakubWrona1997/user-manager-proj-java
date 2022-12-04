package com.usermanagerproj.service.admin;

import com.usermanagerproj.contracts.admin.AdminService;
import com.usermanagerproj.domain.role.Role;
import com.usermanagerproj.domain.user.AppUser;
import com.usermanagerproj.dto.user.request.CreateBasicUserRequest;
import com.usermanagerproj.dto.user.request.UpdateDetailsRequest;
import com.usermanagerproj.dto.user.response.AppUserResponse;
import com.usermanagerproj.event.Event;
import com.usermanagerproj.event.EventType;
import com.usermanagerproj.event.NotificationEventPublisher;
import com.usermanagerproj.exception.EntityNotFoundException;
import com.usermanagerproj.repository.EventRepository;
import com.usermanagerproj.repository.RoleRepository;
import com.usermanagerproj.repository.UserRepository;
import com.usermanagerproj.service.registration.token.ConfirmationToken;
import com.usermanagerproj.service.registration.token.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EventRepository eventRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final PasswordEncoder passwordEncoder;
    private final NotificationEventPublisher notificationEventPublisher;

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
    public List<AppUserResponse> fetchAllActiveUsers(int size, int page) {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<AppUser> appUsers = userRepository.findAllByIsEnabled(true, pageRequest);

        ModelMapper modelMapper = new ModelMapper();
        Type listType = new TypeToken<List<AppUserResponse>>() {}.getType();
        return modelMapper.map(appUsers, listType);
    }

    @Override
    public List<Event> fetchAllEvents() {
        Sort sort = Sort.by(Sort.Direction.DESC, "eventDate");
        return eventRepository.findAll(sort);
    }

    @Override
    public String createUser(CreateBasicUserRequest createBasicUserRequest) {

        AppUser newAppUser = setUpNewUser(new AppUser(), createBasicUserRequest);
        userRepository.save(newAppUser);

        notificationEventPublisher.publishEvent(new Event("User created", newAppUser.getUsername(), EventType.USER_CREATED));

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
    public String blockUser(String userName) {
        try {
            userRepository.blockUser(userName);
            notificationEventPublisher.publishEvent(new Event("User blocked", userName, EventType.USER_BLOCKED));
            return "User blocked";
        }
        catch (Exception e) {
            throw new EntityNotFoundException(userName,AppUser.class);
        }
    }

    @Override
    public String unblockUser(String userName) {
        try {
            userRepository.unblockUser(userName);
            notificationEventPublisher.publishEvent(new Event("User unblocked", userName, EventType.USER_UNBLOCKED));
            return "User unblocked";
        }
        catch (Exception e) {
            throw new EntityNotFoundException(userName,AppUser.class);
        }
    }

    @Override
    public void deleteUser(String userName) {
        try {
            userRepository.disableAppUser(userName);
            notificationEventPublisher.publishEvent(new Event("User deleted", userName, EventType.USER_DELETED));
        } catch (Exception e) {
            throw new EntityNotFoundException(userName, AppUser.class);
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
    private String generateRandomPassword() {
        return new Random().ints(10, 33, 122).mapToObj(i -> String.valueOf((char) i)).collect(Collectors.joining());
    }
}
