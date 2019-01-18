package com.courseproject.movienightsapi.models.calendars;

import com.google.api.client.util.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

public class CalendarEvent {
    @Id
    private String id;
    private String eventId;
    private Long startsAt;
    private Long endsAt;

    public CalendarEvent() {
    }

    public CalendarEvent(String eventId, Long startsAt, Long endsAt) {
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

    public Long getStartsAt() {
        return startsAt;
    }

    public Long getEndsAt() {
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
