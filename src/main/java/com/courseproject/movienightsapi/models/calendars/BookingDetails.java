package com.courseproject.movienightsapi.models.calendars;

import com.google.api.client.util.DateTime;

public class BookingDetails {
    private DateTime startsAt;
    private String id;

    public BookingDetails() {
    }

    public BookingDetails(DateTime startsAt, String id) {
        this.startsAt = startsAt;
        this.id = id;
    }

    public DateTime getStartsAt() {
        return startsAt;
    }

    public String getId() {
        return id;
    }
}
