package com.usermanagerproj.dto.user.request;

import com.usermanagerproj.domain.role.ERole;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class CreateBasicUserRequest {
    private String firstName;

    private String lastName;

    @NotBlank(message = "Email cannot be empty")
    @Email
    private String email;

    @NotBlank(message = "Username cannot be empty")
    private String username;

    private String password;

    private Integer age;

    private ERole role;
}
