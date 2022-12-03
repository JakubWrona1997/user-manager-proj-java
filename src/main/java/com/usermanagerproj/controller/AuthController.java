package com.usermanagerproj.controller;

import com.usermanagerproj.dto.user.request.SignUpRequest;
import com.usermanagerproj.service.registration.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmToken(@RequestParam String token) {
        return new ResponseEntity<>(registrationService.confirmToken(token), HttpStatus.OK);
    }

    @GetMapping("/generatePassword")
    public ResponseEntity<Integer> generateRandomPassword() {
        return new ResponseEntity<>(registrationService.generateRandomNumber(), HttpStatus.OK);
    }
}
