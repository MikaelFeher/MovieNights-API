package com.courseproject.movienightsapi.services;

import com.courseproject.movienightsapi.models.Google.GoogleUserProfile;
import com.courseproject.movienightsapi.models.users.User;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GoogleService {
    @Autowired
    private UserService userService;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String CLIENT_ID;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String CLIENT_SECRET;

    public GoogleUserProfile getUserProfile(GoogleIdToken.Payload payload){
        return new GoogleUserProfile(
                payload.getSubject(),
                payload.getEmail(),
                (String) payload.get("given_name"),
                (String) payload.get("family_name"),
                (String) payload.get("locale"),
                (String) payload.get("picture"),
                Boolean.valueOf(payload.getEmailVerified())
        );
    }

    public Calendar getUserCalendar(User user) {
        GoogleCredential credential = getUserCredential(user);
        return new Calendar.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
                        .setApplicationName("Movie Nights")
                        .build();
    }

    public GoogleCredential getUserCredential(User user){
        if (!isTokenValid(user.getAccessTokenExpiresAt())) refreshToken(user);
        return new GoogleCredential().setAccessToken(user.getAccessToken());
    }

    public void refreshToken(User user) {
        GoogleCredential credential = getRefreshedCredentials(user.getRefreshToken());
        String accessToken = credential.getAccessToken();
        Long tokenExpiresAt = System.currentTimeMillis();
        userService.updateAccessToken(user, accessToken, tokenExpiresAt);
    }

    private Boolean isTokenValid(Long tokenExpiresAt) {
        Long now = System.currentTimeMillis();
        if (tokenExpiresAt < now) return false;
        return true;
    }

    private GoogleCredential getRefreshedCredentials(String refreshCode) {
        try {
            GoogleTokenResponse response = new GoogleRefreshTokenRequest(
                    new NetHttpTransport(), JacksonFactory.getDefaultInstance(), refreshCode, CLIENT_ID, CLIENT_SECRET )
                    .execute();

            return new GoogleCredential().setAccessToken(response.getAccessToken());
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
