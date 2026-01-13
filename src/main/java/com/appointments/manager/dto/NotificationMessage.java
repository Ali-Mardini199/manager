package com.appointments.manager.dto;

import java.time.Instant;

public class NotificationMessage {
    private String title;
    private String content;
    private Instant timestamp;

    public NotificationMessage() {
    }

    public NotificationMessage(String title, String content, Instant timestamp) {
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
