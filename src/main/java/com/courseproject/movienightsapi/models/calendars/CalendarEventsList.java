package com.courseproject.movienightsapi.models.calendars;

import java.util.ArrayList;
import java.util.List;

public class CalendarEventsList {
    private List<CalendarEvent> eventsList = new ArrayList<>();

    public CalendarEventsList() {
    }

    public List<CalendarEvent> getEventsList() {
        return eventsList;
    }

    public void add(CalendarEvent event) {
        this.eventsList.add(event);
    }

    public void printEvents() {
        this.eventsList.stream().forEach(System.out::println);
    }
}
