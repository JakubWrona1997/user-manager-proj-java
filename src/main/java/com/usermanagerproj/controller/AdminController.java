package com.usermanagerproj.controller;


import com.usermanagerproj.contracts.user.UserService;
import com.usermanagerproj.dto.user.response.AppUserResponse;
import com.usermanagerproj.service.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {
    private final UserService userService;

    @GetMapping("/user")
    public ResponseEntity<AppUserResponse> getCurrentDetails(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity<>(userService.fetchUser(userDetails.getUserid()), HttpStatus.OK);
    }
}
