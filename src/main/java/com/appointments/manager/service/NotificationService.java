package com.appointments.manager.service;

import com.appointments.manager.dto.NotificationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void notifyUser(String username, NotificationMessage message) {
        messagingTemplate.convertAndSendToUser(
                username,
                "/queue/notifications",
                message
        );
    }

    public void notifyAll(NotificationMessage message) {
        messagingTemplate.convertAndSend(
                "/topic/notifications",
                message
        );
    }
}
