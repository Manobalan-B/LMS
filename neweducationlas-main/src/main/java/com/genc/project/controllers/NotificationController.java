package com.genc.project.controllers;

import com.genc.project.entities.Notification;
import com.genc.project.entities.User;
import com.genc.project.services.NotificationService;
import com.genc.project.services.UserDetailsImpl;
import com.genc.project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @Autowired
    UserService userService;

    @GetMapping("/notifications")
    public String viewNotifications(Model model, Authentication auth) {
        int id = ((UserDetailsImpl) auth.getPrincipal()).getId();
        User user = userService.findById(id);
        List<Notification> notifications = notificationService.getNotificationsForUser(id);
        model.addAttribute("notifications", notifications);
        model.addAttribute("name",user.getName());
        return "Notifications"; // notification.html
    }
}
