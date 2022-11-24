package com.usermanagerproj.service.registration.email;

public interface EmailSender {
    void send(String to, String email);
}
