package com.courseproject.movienightsapi.controllers;

import com.courseproject.movienightsapi.models.users.User;
import com.courseproject.movienightsapi.repositories.UserRepository;
import com.courseproject.movienightsapi.services.GoogleService;
import com.courseproject.movienightsapi.services.UserService;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class GoogleAuthorizationController {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String CLIENT_SECRET;

    @Autowired
    private UserService userService;

    @Autowired
    private GoogleService googleService;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/storeauthcode", method = RequestMethod.POST)
    public HttpStatus storeauthcode(@RequestBody String code, @RequestHeader("X-Requested-With") String encoding) {
        if (encoding == null || encoding.isEmpty()) {
            // Without the `X-Requested-With` header, this request could be forged. Aborts.
            return HttpStatus.UNAUTHORIZED;
        }

        // Get GoogleTokenResponse...
        GoogleTokenResponse tokenResponse = null;
        try {
            tokenResponse = getGoogleToken(code);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Get profile info from ID token (Obtained at the last step of OAuth2)
        GoogleIdToken idToken = null;
        try {
            idToken = tokenResponse.parseIdToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
        GoogleIdToken.Payload payload = idToken.getPayload();

        // Create a user, assign tokens and save to db...
        userService.createUser(payload, tokenResponse);

        // TODO: Extract functionality to get calendar events to GoogleService.java...
        // TODO: Create functionality to parse available times in the calendar...
        // TODO: Create functionality to plan events...
        // TODO: Create functionality to create planned events in the users calendars...
        // TODO: Create functionality to parse available times in the calendar...

        /******************** EXTRACT TO SEPERATE FILE ********************/

   /*
          List the next 10 events from the primary calendar.
            Instead of printing these with System out, you should of course store them in a database or in-memory variable to use for your application.
        {1}
            The most important parts are:
            event.getSummary()             // Title of calendar event
            event.getStart().getDateTime() // Start-time of event
            event.getEnd().getDateTime()   // Start-time of event
            event.getStart().getDate()     // Start-date (without time) of event
            event.getEnd().getDate()       // End-date (without time) of event
        {1}
            For more methods and properties, see: Google Calendar Documentation.
        *//*
        DateTime now = new DateTime(System.currentTimeMillis());
        Events events = null;
        try {
            events = calendar.events().list("primary")
                    .setMaxResults(10)
                    .setTimeMin(now)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        CalendarEventsList eventsList = new CalendarEventsList();

        List<Event> items = events.getItems();
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

        eventsList.printEvents();

        userService.addUserToDB(new User(
                userId,
                email,
                givenName,
                familyName,
                locale,
                pictureUrl,
                emailVerified,
                accessToken,
                refreshToken,
                expiresAt,
                eventsList
        ));


        *//******************************************************************//*
*/
        return HttpStatus.OK;
    }

    @GetMapping("/refreshtoken/{id}")
    public User refreshToken(@PathVariable String id) {
        User user = userRepository.findById(id).get();
        System.out.println("Accesstoken: " + user.getAccessToken());
        googleService.refreshToken(user);
        return userRepository.findById(id).get();
    }

    private GoogleTokenResponse getGoogleToken(String code) throws IOException {
        return new GoogleAuthorizationCodeTokenRequest(
                new NetHttpTransport(),
                JacksonFactory.getDefaultInstance(),
                "https://www.googleapis.com/oauth2/v4/token",
                CLIENT_ID,
                CLIENT_SECRET,
                code,
                "http://localhost:8080")
                .execute();
    }

}
