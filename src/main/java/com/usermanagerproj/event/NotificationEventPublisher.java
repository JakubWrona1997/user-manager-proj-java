package com.usermanagerproj.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventPublisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishEvent(final Event event) {
        NotificationEvent notificationEvent = new NotificationEvent(this, event);
        applicationEventPublisher.publishEvent(notificationEvent);
    }
}
