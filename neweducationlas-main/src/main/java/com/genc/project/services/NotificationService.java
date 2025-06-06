package com.genc.project.services;


import com.genc.project.entities.Notification;
import com.genc.project.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    public List<Notification> getNotificationsForUser(int id) {
        return notificationRepository.findByUserid(id);
    }

    public void sendNotification(int id, String message, String type) {
        Notification notification = new Notification(message, type,id);
        notificationRepository.save(notification);
    }
}
