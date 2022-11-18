package com.usermanagerproj.service;

import com.usermanagerproj.domain.user.AppUser;
import com.usermanagerproj.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = userRepository.findAppUserByUsername(username).orElseThrow(
                ()-> new UsernameNotFoundException("User not found with username" + username));
        return UserDetailsImpl.build(appUser);
    }

    public UserDetails loadUserById(UUID id) throws UsernameNotFoundException {
        AppUser appUser = userRepository.findById(id).orElseThrow(
                ()-> new UsernameNotFoundException("User not found with id" + id));
        return UserDetailsImpl.build(appUser);
    }
}
