package com.courseproject.movienightsapi.services;

import com.courseproject.movienightsapi.models.calendars.CalendarEvent;
import com.courseproject.movienightsapi.models.users.User;
import com.courseproject.movienightsapi.repositories.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CalendarService {
    @Autowired
    private GoogleService googleService;
    @Autowired
    private UserRepository userRepository;

    public List<CalendarEvent> populateCalendarEventsList(User user) {
        List<CalendarEvent> eventsList = new ArrayList<>();

        List<Event> items = getUserCalendarEvents(user).getItems();
        if (items.isEmpty()) {
            System.out.println("No upcoming events found.");
        } else {
            System.out.println("Upcoming events");
            for (Event event : items) {
                DateTime start = event.getStart().getDateTime();
                if (start == null) { // If it's an all-day-event - store the date instead
                    start = event.getStart().getDate();
                }
                DateTime end = event.getEnd().getDateTime();
                if (end == null) { // If it's an all-day-event - store the date instead
                    end = event.getStart().getDate();
                }

                Long startLong = start.getValue();
                Long endLong =  end.getValue();

                eventsList.add(new CalendarEvent(event.getId(), startLong, endLong));
            }
        }

        return eventsList;
    }

    public Events getUserCalendarEvents(User user) {
        Long start = System.currentTimeMillis();
        Long end = start + 3600 * 24 * 7 * 1000;
        DateTime now = new DateTime(start);
        DateTime oneWeekFromNow = new DateTime(end);

        Calendar calendar = getUserCalendar(user);

        Events events = null;
        try {
            events = calendar.events().list("primary")
                    .setTimeMin(now)
                    .setTimeMax(oneWeekFromNow)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return events;
    }

    public Calendar getUserCalendar(User user) {
        GoogleCredential credential = googleService.getUserCredential(user);
        return new Calendar.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName("Movie Nights")
                .build();
    }

    public void findAvailableDates() {
        Long oneDay = 3600 * 24L * 1000;
        Long twoHours = oneDay/12;
        Long oneHour = twoHours/2;
        Long sevenOClock = oneHour * 7;
        Long elevenOClock = oneHour * 11;

        Long start = System.currentTimeMillis();
        Long end = start + 3600 * 24 * 7 * 1000;

        DateTime day;
        DateTime hour;
        // TODO: The date and hour calculation works! Now needs to be compared with events in users calendar and displayed in a better way...
        for (Long i = start; i < end; i += oneDay) {
            for (Long j = 0L; j < oneDay - 1000; j += oneHour) {
                System.out.println("Day: " + LocalDateTime.ofInstant(Instant.ofEpochMilli(i), ZoneId.systemDefault()).toLocalDate());
                System.out.println("Hour: " + LocalTime.ofSecondOfDay(j/1000));
            }
        }

    }
}
