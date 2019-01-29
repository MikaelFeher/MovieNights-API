package com.courseproject.movienightsapi.models.calendars;

import com.courseproject.movienightsapi.models.movies.Movie;
import com.google.api.client.util.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "PlannedEvents")
public class PlannedEvent {
    @Id
    private String id;
    private DateTime start;
    private DateTime end;
    private Movie movie;

    public PlannedEvent() {
    }

    public PlannedEvent(DateTime start, Movie movie) {
        this.start = start;
        this.end = new DateTime(this.start.getValue() + 7200 * 1000);
        this.movie = movie;
    }

    public DateTime getStart() {
        return start;
    }

    public DateTime getEnd() {
        return end;
    }

    public Movie getMovie() {
        return movie;
    }
}
