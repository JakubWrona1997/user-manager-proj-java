package com.usermanagerproj.config.manager;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthOneWayFunction extends CustomAuthenticationManagerDecorator {
    private final CacheManager cacheManager;
    public CustomAuthOneWayFunction(CustomAuthenticationManager customAuthenticationManager, CacheManager cacheManager) {
        super(customAuthenticationManager);
        this.cacheManager = cacheManager;
    }

    public Authentication authWithOneWayFunction(Authentication authentication, double result){
        Authentication authResult;
        authResult = super.authenticate(authentication);
        if(!((Math.round(((double)authentication.getName().length() / getRandomNumber()) * 100.0)/100.0) == result)){
            authResult.setAuthenticated(false);
            throw new BadCredentialsException("Invalid password or username");
        }
        return authResult;
    }
    public int getRandomNumber(){
        Cache number = cacheManager.getCache("randomNumber");
        if (number != null && number.get("generateRandomNumber") == null) {
            throw new BadCredentialsException("Invalid password or username");
        }
        return (int) number.get("generateRandomNumber").get();
    }
}
