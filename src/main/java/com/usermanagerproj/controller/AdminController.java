package com.usermanagerproj.controller;


import com.usermanagerproj.contracts.admin.AdminService;
import com.usermanagerproj.contracts.user.UserService;
import com.usermanagerproj.dto.user.response.AppUserResponse;
import com.usermanagerproj.service.user.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@PreAuthorize("hasRole('ROLE_SUPERADMIN')")
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final UserService userService;

    private final AdminService adminService;

    @GetMapping("/user")
    public ResponseEntity<AppUserResponse> getCurrentDetails(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity<>(userService.fetchUser(userDetails.getUserid()), HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<List<AppUserResponse>> getAllUsers(@RequestParam int size, @RequestParam int page) {
        return new ResponseEntity<>(adminService.fetchAllActiveUsers(size, page), HttpStatus.OK);
    }
}
