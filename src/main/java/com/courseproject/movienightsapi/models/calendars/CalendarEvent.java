package com.courseproject.movienightsapi.models.calendars;

import com.google.api.client.util.DateTime;
import org.springframework.data.annotation.Id;

public class CalendarEvent {
    @Id
    private String id;
    private String eventId;
    private DateTime startsAt;
    private DateTime endsAt;

    public CalendarEvent(String eventId, DateTime startsAt, DateTime endsAt) {
        this.eventId = eventId;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
    }

    public String getId() {
        return id;
    }

    public String getEventId() {
        return eventId;
    }

    public DateTime getStartsAt() {
        return startsAt;
    }

    public DateTime getEndsAt() {
        return endsAt;
    }

    @Override
    public String toString() {
        return "CalendarEvent{" +
                "eventId= '" + eventId + '\'' +
                ", startsAt= " + startsAt +
                ", endsAt= " + endsAt +
                '}';
    }
}
