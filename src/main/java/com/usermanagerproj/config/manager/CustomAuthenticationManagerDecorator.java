package com.usermanagerproj.config.manager;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@AllArgsConstructor
public abstract class CustomAuthenticationManagerDecorator implements AuthenticationManager {
    private AuthenticationManager authenticationManager;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return authenticationManager.authenticate(authentication);
    }
}
