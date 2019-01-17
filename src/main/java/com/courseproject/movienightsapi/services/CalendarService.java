package com.courseproject.movienightsapi.services;

import com.courseproject.movienightsapi.models.calendars.CalendarEvent;
import com.courseproject.movienightsapi.models.calendars.CalendarEventsList;
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
import java.util.List;

@Service
public class CalendarService {
    @Autowired
    private GoogleService googleService;

    // TODO: Change userId in parameter to Id in all occurrences...
    // TODO: FIX below code...
    public CalendarEventsList populateCalendarEventsList(String userId) {
        CalendarEventsList eventsList = new CalendarEventsList();

        List<Event> items = getUserCalendarEvents(userId).getItems();
        if (items.isEmpty()) {
            System.out.println("No upcoming events found.");
        } else {
            System.out.println("Upcoming events");
            for (com.google.api.services.calendar.model.Event event : items) {
                DateTime start = event.getStart().getDateTime();
                if (start == null) { // If it's an all-day-event - store the date instead
                    start = event.getStart().getDate();
                }
                DateTime end = event.getEnd().getDateTime();
                if (end == null) { // If it's an all-day-event - store the date instead
                    end = event.getStart().getDate();
                }
                System.out.printf("%s (%s) -> (%s)\n", event.getSummary(), start, end);
                System.out.println(event);

                eventsList.add(new CalendarEvent(event.getId(), start, end));
            }
        }

        return eventsList;
    }

    public Events getUserCalendarEvents(String userId) {
        DateTime now = new DateTime(System.currentTimeMillis());
        DateTime sevenDaysFromNow = new DateTime(System.currentTimeMillis() + 60 * 60 * 24 * 7 * 1000);

        Calendar calendar = getUserCalendar(userId);

        Events events = null;
        try {
            events = calendar.events().list("primary")
                    .setTimeMin(now)
                    .setTimeMax(sevenDaysFromNow)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return events;
    }

    public Calendar getUserCalendar(String userId) {
        GoogleCredential credential = googleService.getUserCredential(userId);
        return new Calendar.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName("Movie Nights")
                .build();
    }
}
