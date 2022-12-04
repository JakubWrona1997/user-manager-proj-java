package com.usermanagerproj.event;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String message;
    @Column
    private String userName;
    @Column
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Column
    private LocalDateTime eventDate = LocalDateTime.now();

    public Event(String message, String userName, EventType eventType) {
        this.message = message;
        this.userName = userName;
        this.eventType = eventType;
    }
}
