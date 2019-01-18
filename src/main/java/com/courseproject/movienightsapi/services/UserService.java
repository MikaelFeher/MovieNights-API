package com.courseproject.movienightsapi.services;

import com.courseproject.movienightsapi.models.Google.Tokens;
import com.courseproject.movienightsapi.models.calendars.CalendarEvent;
import com.courseproject.movienightsapi.models.users.User;
import com.courseproject.movienightsapi.repositories.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CalendarService calendarService;
    private Tokens tokens;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(GoogleIdToken.Payload payload, GoogleTokenResponse tokenResponse) {
        User newUser = new User(
            payload.getSubject(),
            payload.getEmail(),
            (String) payload.get("given_name"),
            (String) payload.get("family_name"),
            (String) payload.get("locale"),
            (String) payload.get("picture"),
            Boolean.valueOf(payload.getEmailVerified())
        );
        Tokens tokens = new Tokens(tokenResponse);
        setUserTokens(newUser, tokens);
        calendarService.populateCalendarEventsList(newUser);
        addUserToDB(newUser);
        return newUser;
    }

    private void setUserTokens(User user, Tokens tokens) {
        user.setAccessToken(tokens.getAccessToken());
        user.setRefreshToken(tokens.getRefreshToken());
        user.setAccessTokenExpiresAt(tokens.getExpiresAt());
    }

    private void addUserToDB(User user) {
        if (!userRepository.existsByUserId(user.getUserId())) {
            userRepository.save(user);
        }
    }

    public void updateAccessToken(User user, String accessToken, Long accessTokenExpiresAt) {
        user.setAccessToken(accessToken);
        user.setAccessTokenExpiresAt(accessTokenExpiresAt);
        userRepository.save(user);
    }

    public void updateEventsList(User user, List<CalendarEvent> eventsList) {
        user.setEventList(eventsList);
        userRepository.save(user);
    }
}
