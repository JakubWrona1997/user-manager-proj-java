package com.usermanagerproj.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NotificationEvent extends ApplicationEvent {

    private final Event event;
    public NotificationEvent(Object source, Event event) {
        super(source);
        this.event = event;
    }
}
