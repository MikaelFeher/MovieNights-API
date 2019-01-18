package com.courseproject.movienightsapi.services;

import com.courseproject.movienightsapi.models.users.User;
import com.courseproject.movienightsapi.repositories.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GoogleService {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String CLIENT_ID;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String CLIENT_SECRET;

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
        if (tokenExpiresAt < now) {
            return false;
        }
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
