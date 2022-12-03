package com.usermanagerproj.config.security;

public class SecurityConstants {
    public static final String REGISTER_URL = "/api/v1/auth/register";
    public static final String LOGIN_URL = "/api/v1/auth/login";
    public static final String CONFIRM_URL_REGEX = "/api/v1/auth/confirm\\?token=([A-Za-z0-9]+(-[A-Za-z0-9]+)+)";
    public static final String GENERATE_PASSWORD_URL = "/api/v1/auth/generatePassword";
    public static final String BEARER = "Bearer ";
    public static final String AUTHORIZATION = "Authorization";
}
