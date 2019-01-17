package com.courseproject.movienightsapi.controllers;

import com.courseproject.movienightsapi.services.CalendarService;
import com.google.api.services.calendar.model.Events;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calendar")
public class CalendarController {
    @Autowired
    private CalendarService calendarService;

    @GetMapping("/getuserevents/{userId}")
    public Events getUserCalendarEvents(@PathVariable String userId) {
        return calendarService.getUserCalendarEvents(userId);
    }
}
