package com.epam.ui;

import com.epam.domain.CinemaEvent;
import com.epam.domain.User;
import com.epam.service.AuditoriumService;
import com.epam.service.EventService;
import com.epam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("ApplicationController")
public class ApplicationController {

    @Autowired
    private UserService userService;
    @Autowired
    private EventService eventService;
    @Autowired
    private AuditoriumService auditoriumService;

    public List<CinemaEvent> getEvents() {
        return eventService.getAll();
    }

    public List<User> getUsers() {
        return userService.getAll();
    }

    public void insertUser(User user) {
        userService.insert(user);
    }

    public void insertEvent(CinemaEvent event) {
        eventService.insert(event);
    }
}
