package com.usermanagerproj.controller;

import com.usermanagerproj.contracts.user.UserService;
import com.usermanagerproj.dto.user.request.SignUpRequest;
import com.usermanagerproj.service.registration.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final RegistrationService registrationService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        return new ResponseEntity<>(registrationService.register(signUpRequest), HttpStatus.CREATED);
    }
}
