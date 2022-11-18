package com.usermanagerproj.config.manager;

import com.usermanagerproj.service.UserDetailsImpl;
import com.usermanagerproj.service.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CustomAuthenticationManager implements AuthenticationManager {
    private UserDetailsServiceImpl userDetailsService;
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetailsImpl appUser = (UserDetailsImpl) userDetailsService.loadUserByUsername(authentication.getName());
        if(!passwordEncoder.matches(authentication.getCredentials().toString(), appUser.getPassword()) && !appUser.isEnabled()){
            throw new BadCredentialsException("Invalid password or username");
        }
        if(!appUser.isAccountNonLocked()){
            throw new BadCredentialsException("Your account has been blocked, contact the administrator");
        }
        return new UsernamePasswordAuthenticationToken(appUser.getUserid(), authentication.getCredentials().toString(), appUser.getAuthorities());
    }
}
