package com.courseproject.movienightsapi.controllers;

import com.courseproject.movienightsapi.models.calendars.BookingDetails;
import com.courseproject.movienightsapi.models.movies.Movie;
import com.courseproject.movienightsapi.models.users.User;
import com.courseproject.movienightsapi.repositories.MovieRepository;
import com.courseproject.movienightsapi.repositories.UserRepository;
import com.courseproject.movienightsapi.services.CalendarService;
import com.courseproject.movienightsapi.services.UserService;
import com.google.api.client.util.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/calendar")
public class CalendarController {
    @Autowired
    private CalendarService calendarService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private MovieRepository movieRepository;

    @GetMapping("/getuserevents/{id}")
    public void getUserCalendarEvents(@PathVariable String id) {
        User user = userRepository.findById(id).get();
        userService.updateEventsList(user, calendarService.populateCalendarEventsList(user));
    }

    @GetMapping("/availabletimes")
    public ResponseEntity<?> getAvailableTimes(){
        return new ResponseEntity<>(calendarService.findAvailableTimes(), HttpStatus.OK);
    }

    @PostMapping("/createevent")
    public void createEvent(@RequestBody BookingDetails details) throws NullPointerException {
        Movie movie = movieRepository.findById(details.getId()).get();
        DateTime startsAt = details.getStartsAt();
        try{
            calendarService.createCalendarEvent(startsAt, movie);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
