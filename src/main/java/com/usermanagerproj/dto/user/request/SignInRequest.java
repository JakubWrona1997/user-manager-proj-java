package com.usermanagerproj.dto.user.request;

import lombok.Data;

@Data
public class SignInRequest {
    private String username;
    private String password;
    private double result;
}
