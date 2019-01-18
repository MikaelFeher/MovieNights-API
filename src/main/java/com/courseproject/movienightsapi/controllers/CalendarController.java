package com.courseproject.movienightsapi.controllers;

import com.courseproject.movienightsapi.models.calendars.TimeSlot;
import com.courseproject.movienightsapi.models.users.User;
import com.courseproject.movienightsapi.repositories.UserRepository;
import com.courseproject.movienightsapi.services.CalendarService;
import com.courseproject.movienightsapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/calendar")
public class CalendarController {
    @Autowired
    private CalendarService calendarService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @GetMapping("/getuserevents/{id}")
    public void getUserCalendarEvents(@PathVariable String id) {
        User user = userRepository.findById(id).get();
        userService.updateEventsList(user, calendarService.populateCalendarEventsList(user));
    }

    @GetMapping("/availabletimes")
    public List<TimeSlot> getAvailableTimes(){
        return calendarService.findAvailableTimes();
    }
}
