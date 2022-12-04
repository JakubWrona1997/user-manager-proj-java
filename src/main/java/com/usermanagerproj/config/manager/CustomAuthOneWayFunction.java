package com.usermanagerproj.config.manager;

import com.usermanagerproj.event.Event;
import com.usermanagerproj.event.EventType;
import com.usermanagerproj.event.NotificationEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomAuthOneWayFunction extends CustomAuthenticationManagerDecorator {
    private final CacheManager cacheManager;
    private final NotificationEventPublisher notificationEventPublisher;
    public CustomAuthOneWayFunction(CustomAuthenticationManager customAuthenticationManager, CacheManager cacheManager, NotificationEventPublisher notificationEventPublisher) {
        super(customAuthenticationManager);
        this.cacheManager = cacheManager;
        this.notificationEventPublisher = notificationEventPublisher;
    }

    public Authentication authWithOneWayFunction(Authentication authentication, double result){
        Authentication authResult;
        authResult = super.authenticate(authentication);
        if(!((Math.round(((double)authentication.getName().length() / getRandomNumber()) * 100.0)/100.0) == result)){
            authResult.setAuthenticated(false);
            throw new BadCredentialsException("Invalid password or username");
        }
        notificationEventPublisher.publishEvent(new Event("User authenticated", authentication.getName(), EventType.LOGGED_IN));
        return authResult;
    }
    public int getRandomNumber(){
        Cache number = cacheManager.getCache("randomNumber");
        if (number == null || number.get("generateRandomNumber") == null) {
            log.info("Cache is null, please generate random number for authentication");
            throw new RuntimeException("Something went wrong");
        }
        return (int) number.get("generateRandomNumber").get();
    }
}
