package com.courseproject.movienightsapi.services;

import com.courseproject.movienightsapi.models.calendars.CalendarEvent;
import com.courseproject.movienightsapi.models.calendars.PlannedEvent;
import com.courseproject.movienightsapi.models.calendars.TimeSlot;
import com.courseproject.movienightsapi.models.movies.Movie;
import com.courseproject.movienightsapi.models.users.User;
import com.courseproject.movienightsapi.repositories.PlannedEventRepository;
import com.courseproject.movienightsapi.repositories.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CalendarService {
    @Autowired
    private GoogleService googleService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private PlannedEventRepository plannedEventRepository;
    private List<User> users;

    public void createCalendarEvent(DateTime startsAt, Movie movie) throws IOException, NullPointerException {
        users = userRepository.findAll();

        Event event = new Event()
                .setSummary("Movie night: " + movie.getTitle())
                .setLocation("My house")
                .setDescription("Movie and popcorn");

        DateTime startDateTime = new DateTime(startsAt.getValue());
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone(ZonedDateTime.now().getZone().toString());
        event.setStart(start);

        Long twoHours = startsAt.getValue() + (3600 * 2) * 1000;

        DateTime endDateTime = new DateTime(twoHours);
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone(ZonedDateTime.now().getZone().toString());
        event.setEnd(end);

        EventAttendee[] attendees = new EventAttendee[users.size()];

        for (int i = 0; i < users.size(); i++) {
            attendees[i] = new EventAttendee().setEmail(users.get(i).getEmail());
        }

        event.setAttendees(Arrays.asList(attendees));

        String calendarId = "primary";
        event = this.getUserCalendar(users.get(0)).events().insert(calendarId, event).execute();
        plannedEventRepository.save(new PlannedEvent(startsAt, movie));

        System.out.printf("Event created: %s\n", event.getHtmlLink());

    }

    public List<CalendarEvent> populateCalendarEventsList(User user) {
        List<CalendarEvent> eventsList = new ArrayList<>();

        List<Event> items = getUserCalendarEvents(user).getItems();
        if (items.isEmpty()) {
            System.out.println("No upcoming events found.");
        } else {
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

    public Calendar getUserCalendar(User user) throws NullPointerException {
        try {
            GoogleCredential credential = googleService.getUserCredential(user);
            return new Calendar.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
                    .setApplicationName("Movie Nights")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<TimeSlot> findAvailableTimes() {
        List<User> users = userRepository.findAll();
        List<TimeSlot> userEvents = new ArrayList<>();
        List<TimeSlot> availableTimes = new ArrayList<>();

        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.of(19, 00));
        LocalDateTime end = LocalDateTime.of(start.plusWeeks(1).toLocalDate(), LocalTime.of(23, 59));

        populateUserEventsList(users, userEvents);

        // Populate availableTimes...
        while(start.isBefore(end)) {
            availableTimes.add(new TimeSlot(start));
            start = start.plusHours(1);
        }

        // Filter out timeslots before 19:00...
        availableTimes = availableTimes.stream().filter(timeSlot -> timeSlot.getStart().toLocalTime().isAfter(LocalTime.of(18, 00))).collect(Collectors.toList());

        for (int i  = 0; i < availableTimes.size(); i++) {
            for (int j = 0; j < userEvents.size(); j++) {
                if ((availableTimes.get(i).getStart().isAfter(userEvents.get(j).getStart()) && availableTimes.get(i).getEnd().isBefore(userEvents.get(j).getEnd())) ||
                    (availableTimes.get(i).getStart().isBefore(userEvents.get(j).getStart()) && availableTimes.get(i).getEnd().isBefore(userEvents.get(j).getEnd())) ||
                    (availableTimes.get(i).getStart().isBefore(userEvents.get(j).getStart()) && availableTimes.get(i).getEnd().isBefore(userEvents.get(j).getEnd().plusHours(1)))) {

                    availableTimes.remove(i);
                }
            }
        }
        return availableTimes;
    }

    private void populateUserEventsList(List<User> users, List<TimeSlot> userEvents){
        users.stream().forEach(user -> {
            user.getEventList()
                    .stream()
                    .forEach(calendarEvent -> {
                        userEvents.add(new TimeSlot(getLDTFromMilliseconds(calendarEvent.getStartsAt()).truncatedTo(ChronoUnit.HOURS), getLDTFromMilliseconds(calendarEvent.getEndsAt()).truncatedTo(ChronoUnit.HOURS)));
                    });
        });
    }

    private LocalDateTime getLDTFromMilliseconds(Long value) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneId.systemDefault());
    }

}
