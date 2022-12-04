package com.usermanagerproj.event;

import com.usermanagerproj.repository.EventRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class NotificationEventListener implements ApplicationListener<NotificationEvent> {
    EventRepository eventRepository;

    @Override
    public void onApplicationEvent(NotificationEvent notificationEvent) {
        System.out.println("Handling event...");
        eventRepository.save(notificationEvent.getEvent());
    }
}
