package com.usermanagerproj.controller;


import com.usermanagerproj.contracts.admin.AdminService;
import com.usermanagerproj.contracts.user.UserService;
import com.usermanagerproj.dto.user.request.CreateBasicUserRequest;
import com.usermanagerproj.dto.user.response.AppUserResponse;
import com.usermanagerproj.event.Event;
import com.usermanagerproj.service.user.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @GetMapping("/currentUser")
    public ResponseEntity<AppUserResponse> getCurrentDetails(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity<>(userService.fetchUser(userDetails.getUserid()), HttpStatus.OK);
    }

    @GetMapping("/getUser")
    public ResponseEntity<AppUserResponse> getUser(@RequestParam String userName) {
        return new ResponseEntity<>(userService.fetchUser(userName), HttpStatus.OK);
    }

    @PutMapping("/blockUser")
    public ResponseEntity<String> blockUser(@RequestParam String userName) {
        return new ResponseEntity<>(adminService.blockUser(userName), HttpStatus.OK);
    }

    @PutMapping("/unblockUser")
    public ResponseEntity<String> unblockUser(@RequestParam String userName) {
        return new ResponseEntity<>(adminService.unblockUser(userName), HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<List<AppUserResponse>> getAllUsers(@RequestParam int size, @RequestParam int page) {
        return new ResponseEntity<>(adminService.fetchAllActiveUsers(size, page), HttpStatus.OK);
    }

    @PostMapping("/user")
    public ResponseEntity<String> createUser(@RequestBody CreateBasicUserRequest createBasicUserRequest) {
        return new ResponseEntity<>(adminService.createUser(createBasicUserRequest), HttpStatus.CREATED);
    }

    @DeleteMapping("/user")
    public ResponseEntity<String> deleteUser(@RequestParam String username) {
        adminService.deleteUser(username);
        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/events")
    public ResponseEntity<List<Event>> getEvents() {
        return new ResponseEntity<>(adminService.fetchAllEvents(), HttpStatus.OK);
    }
}
