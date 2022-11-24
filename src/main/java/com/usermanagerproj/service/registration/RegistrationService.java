package com.usermanagerproj.service.registration;

import com.usermanagerproj.contracts.user.UserService;
import com.usermanagerproj.domain.user.AppUser;
import com.usermanagerproj.dto.user.request.SignUpRequest;
import com.usermanagerproj.service.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {
    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;

    public String register(SignUpRequest request) {

        AppUser appUser = mapRequestToAppUser(request);

        return userService.saveUser(appUser);
    }

    private AppUser mapRequestToAppUser(SignUpRequest request) {
        AppUser appUser = new AppUser();
        appUser.setUsername(request.getUsername());
        appUser.setPassword(request.getPassword());
        appUser.setEmail(request.getEmail());
        appUser.setFirstName(request.getFirstName());
        appUser.setLastName(request.getLastName());
        appUser.setAge(request.getAge());
        return appUser;
    }

}
