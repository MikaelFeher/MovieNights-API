package com.courseproject.movienightsapi.services;

import com.courseproject.movienightsapi.models.Google.Tokens;
import com.courseproject.movienightsapi.models.users.User;
import com.courseproject.movienightsapi.repositories.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;
    private Tokens tokens;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(GoogleIdToken.Payload payload, GoogleTokenResponse tokenResponse) {
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
        addUserToDB(newUser);
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
        User userToUpdate = userRepository.findById(user.getId()).get();
        userToUpdate.setAccessToken(accessToken);
        userToUpdate.setAccessTokenExpiresAt(accessTokenExpiresAt);
        userRepository.save(userToUpdate);
    }

    public User findUser(String userId) {
        return userRepository.findByUserId(userId);
    }
}
