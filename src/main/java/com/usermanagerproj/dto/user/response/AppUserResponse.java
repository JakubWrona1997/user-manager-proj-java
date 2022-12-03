package com.usermanagerproj.dto.user.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AppUserResponse {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private Integer age;
    private Boolean isBlocked;
    private Set<RoleDto> roles;
}
