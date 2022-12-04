package com.usermanagerproj.controller;

import com.usermanagerproj.contracts.user.UserService;
import com.usermanagerproj.dto.user.request.ChangePasswordRequest;
import com.usermanagerproj.service.user.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@PreAuthorize("hasRole('ROLE_USER, ROLE_ADMIN')")
@RequestMapping("/api/v1/user")
public class UserController {
    UserService userService;

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                               @Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        return new ResponseEntity<>(userService.changePassword(userDetails.getUserid(),changePasswordRequest), HttpStatus.CREATED);
    }
}
